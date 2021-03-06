package ovh.spajste.yeti;

import com.fazecast.jSerialComm.SerialPort;

import javax.xml.bind.DatatypeConverter;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ELMInterface is class providing an abstraction layer with ELM327 interface.
 * It takes care of serial communication, {@link Readout} management and communication error handling.
 *
 * @author Sebastian Madejski (@mwht)
 * @see Readout
 * @see SerialCommunication
 */
public class ELMInterface {
    /**
     * Identification string reported by ELM interface.
     * For convenience, it will be referred on as name.
     */
    private String name;
    private String currentProtocol;
    private List<Readout> readouts;
    private SerialCommunication serialCommunication;
    private SerialPort serialPort;
    private String selectedPort;
    private Thread readoutDispatchThread;
    private boolean readyToClose;
    private boolean writeToFile;
    private FileOutputStream fos;
    private ObjectOutputStream oos;

	private enum Protocol {
        AUTOMATIC,
        SAE_J1850_PWM,
        SAE_J1850_VPW,
        ISO_9141_2,
        ISO_14230_4_KWP_5_BAUD,
        ISO_14230_4_KWP_FAST_INIT,
        ISO_15765_4_CAN_11BIT_ID_500KBAUD,
        ISO_15765_4_CAN_29BIT_ID_500KBAUD,
        ISO_15765_4_CAN_11BIT_ID_250KBAUD,
        ISO_15765_4_CAN_29BIT_ID_250KBAUD
    };

    private enum ConnectionState {
        NOT_INITIALIZED,
        INITIALIZING,
        CONNECTED,
        CLOSING,
        CLOSED,
        ERROR
    };
    private ConnectionState currentState;
    /**
     * Constructor for ELMInterface.
     */
    public ELMInterface() {
        name = "Unknown";
        currentProtocol = "Not initialized";
        readouts = new ArrayList<>();
        serialCommunication = new SerialCommunication();
        selectedPort = "";
        currentState = ConnectionState.NOT_INITIALIZED;
    }

    /**
     * Initialize connection with ELM interface.
     * Method initializes connection with ELM interface,
     * performs query for PIDs supported by ECU and stores them
     * in {@link Readout} list.
     *
     * @param portName ELM interface serial port name
     */
    public void initialize(String portName) {
    	//mockLoop();
    	//if(true) {
	        selectedPort = portName; // set port name
	        try {
	            currentState = ConnectionState.INITIALIZING;
	            serialPort = serialCommunication.selectPort(portName); // select serial port specified in portName param
	            serialCommunication.openConnection(serialPort,38400); // initialize select port with 38400 baud rate
				Initialization init = new AutoInitialization(serialPort, serialCommunication);
				try {
					init.initialize();
				} catch(Exception e) {
					System.err.println("Error occured during initialization: " + e.getClass().getSimpleName() + ": " + e.getMessage());
				}
				System.out.println("[ELM] Known readouts:");
				init.availablePIDS.forEach((pid) -> {
					if(Readouts.readoutMap.containsKey(pid)) {
						Class<?> readoutClass = Readouts.readoutMap.get(pid);
						System.out.println(readoutClass.getSimpleName());
						try {
							readouts.add((Readout) readoutClass.newInstance());
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				});
	            //readouts.add(new RPMReadout());
	            readoutDispatchThread = new Thread(new Runnable() {
	                @Override
	                public void run() {

	                        while(currentState != ConnectionState.CLOSING) {
								try {
									readouts.forEach((readout -> {
										if(readout.isActive()) {
											try {
												byte[] tmp = new byte[1];
												tmp[0] = readout.getPid();
												serialCommunication.sendData(serialPort, ("01" + DatatypeConverter.printHexBinary(tmp) + "\n").getBytes());
												String rawData = new String(serialCommunication.waitAndReadData(serialPort));
												byte[] elmData = convertELMdataToByteArray(extractData(rawData));
												System.out.println(elmData.length);
												if (elmData.length > 2) {
													if (elmData[1] == readout.getPid()) {
														readout.setReadoutBuffer(Arrays.copyOfRange(elmData, 2, elmData.length));
													}
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}));
									if(writeToFile) {
										oos.writeLong(System.currentTimeMillis());
										oos.writeObject(readouts);
									}
									Thread.sleep(666);
	                        } catch(Exception e) {
									System.err.println("Exception in readout dispatcher: "+e.getClass().getName()+" - "+e.getMessage());
								}
	                  
	                    }
	                    currentState = ConnectionState.CLOSED;
	                }
	            });
	            readoutDispatchThread.start();
	        } catch(Exception e) {
	            System.err.println("Exception caught in ELMInterface: "+e.getClass().getName()+" - "+e.getMessage());
	            currentState = ConnectionState.ERROR;
	        }
    	//}
    }

    public void startWriteToFile() {
    	if(!writeToFile) {
			try {
				fos = new FileOutputStream(System.currentTimeMillis() + ".yeti");
				oos = new ObjectOutputStream(fos);
				writeToFile = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
    		System.out.println("Already logging to file.");
		}
	}

	public void stopWriteToFile() {
		if(writeToFile) {
			try {
				oos.flush();
				oos.close();
				fos.close();
				writeToFile = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Not logging to file.");
		}
	}
    
    public List<Readout> getReadoutsData() {
    	ArrayList<Readout> newReadouts = new ArrayList<>();
	readouts.forEach((elem) -> {
		newReadouts.add(elem);
	});
	return newReadouts;
    }

	private String extractData(String input) {
	try {
        	Pattern bytePattern = Pattern.compile("[0-9A-F]{2}\\s");
        	Matcher byteMatcher = bytePattern.matcher(input);
        	String result = "";
        	while(byteMatcher.find()) {
            		result += byteMatcher.group();
	        }
        	return result.trim();
	} catch(Exception e) {
	    	return "00 00";
	}
    }

    public void close() {
        try {
			readoutDispatchThread.interrupt();
			currentState = ConnectionState.CLOSING;
        	//while(currentState == ConnectionState.CLOSING) {}
	        serialCommunication.sendData(serialPort,"AT PC\n".getBytes());
	        serialCommunication.closeConnection(serialPort);
	        currentState = ConnectionState.CLOSED;
			if(writeToFile) {
				stopWriteToFile();
			}
        } catch(Exception e) {
            System.err.println("Exception caught in ELMInterface: "+e.getClass().getName()+" - "+e.getMessage());
        }
    }

    public static byte[] convertELMdataToByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s.replace(" ", "").replace("\n", "").replace("\r",""));
    }

    public String getName() {
        return name;
    }

    public String getCurrentProtocol() {
        return currentProtocol;
    }
}
