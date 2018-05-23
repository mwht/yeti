package ovh.spajste.yeti;

import com.fazecast.jSerialComm.SerialPort;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.List;
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
    private Thread receiveDataThread;
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
            } else if (Pattern.matches("^AUTO, (.*)",fullResponse)) {

            }

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
