package ovh.spajste.yeti;

import com.fazecast.jSerialComm.SerialPort;

import javax.xml.bind.DatatypeConverter;
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
     * For convinience, it will be referred on as name.
     */
    private String name;
    private String currentProtocol;
    private List<Readout> readouts;
    private SerialCommunication serialCommunication;
    private SerialPort serialPort;
    private String selectedPort;
    private Thread readoutDispatchThread;
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

    /**
     * Constructor for ELMInterface.
     */
    public ELMInterface() {
        name = "Unknown";
        currentProtocol = "Not initialized";
        readouts = new ArrayList<>();
        serialCommunication = new SerialCommunication();
        selectedPort = "";
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
        selectedPort = portName; // set port name
        try {
            serialPort = serialCommunication.selectPort(portName); // select serial port specified in portName param
            serialCommunication.openConnection(serialPort,38400); // initialize select port with 38400 baud rate

            serialCommunication.sendData(serialPort,"ATZ\n".getBytes()); // query ELM for identification string
            name = new String(serialCommunication.waitAndReadData(serialPort)); // read ELM ID string

            serialCommunication.sendData(serialPort,"AT SP 0\n".getBytes());
            String fullResponse = new String(serialCommunication.waitAndReadData(serialPort));
            if(Pattern.matches("/FAIL/",fullResponse)) {
                // TODO: iterate through all protocol until success (@mwht)
            } else {
                Matcher match = Pattern.compile("^AUTO, (.*)").matcher(fullResponse);
                if(match.groupCount() > 0) {
                    currentProtocol = match.group(1);
                } else {
                    throw new Exception("Cannot initialize ELM interface - no matching protocol found");
                }
            }

            // --- read down all PIDs ---
            serialCommunication.sendData(serialPort,"0100\n".getBytes());
            serialCommunication.waitAndReadData(serialPort); // INIT...
            serialCommunication.waitAndReadData(serialPort); // OK/FAIL

            // TODO: readout all pids
            /*byte[] pidRawData = convertELMdataToByteArray(new String(serialCommunication.waitAndReadData(serialPort)));
            if(((pidRawData[0] & 0xF0) >> 4) == 4 && pidRawData[1] == 0x00) {
                if(pidRawData.length == 6) {
                    byte[] pidArray = Arrays.copyOfRange(pidRawData,2,6);
                    int pids = (pidArray[0] << 24) | (pidArray[1] << 16) | (pidArray[2] << 8) | pidArray[3];

                }
            } else {
                System.err.println("fail");
            }*/
            serialCommunication.waitAndReadData(serialPort);
            readouts.add(new RPMReadout());
            readoutDispatchThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String rawData = new String(serialCommunication.waitAndReadData(serialPort));
                        if(Pattern.matches("/[0-9A-F]{2}\\s?/g",rawData)) {
                            byte[] elmData = convertELMdataToByteArray(rawData.trim());
                            readouts.forEach((readout) -> {
                                if (elmData[1] == readout.getPid()) {
                                    readout.setReadoutBuffer(Arrays.copyOfRange(elmData, 2, elmData.length));
                                    System.out.println(readout.getName() + ": " + readout.getValue());
                                }
                            });
                        }
                        Thread.sleep(100);
                    } catch(Exception e) {
                        System.err.println("Exception in readout dispatcher: "+e.getClass().getName()+" - "+e.getMessage());
                    }
                }
            });
        } catch(Exception e) {
            System.err.println("Exception caught in ELMInterface: "+e.getClass().getName()+" - "+e.getMessage());
        }
    }

    public static byte[] convertELMdataToByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s.replace(" ",""));
    }

    public String getName() {
        return name;
    }

    public String getCurrentProtocol() {
        return currentProtocol;
    }
}
