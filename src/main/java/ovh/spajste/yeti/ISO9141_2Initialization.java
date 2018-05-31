package ovh.spajste.yeti;

import com.fazecast.jSerialComm.SerialPort;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

public class ISO9141_2Initialization extends Initialization {

    ISO9141_2Initialization(SerialPort serialPort, SerialCommunication serialCommunication)
    {
        super(serialPort, serialCommunication);
        protocolName = "ISO 9141-2";
        protocolSelectSequence = "AT SP 3\n";
        expectedSelectProtocolAnswer = "OK\n" + "\n" + ">";
    }

    @Override
    public void reciveProtocolSelectAnswer() throws InitializationException, PortNotOpenException
    {
        try
        {
            byte[] protoclolAnswer = serialCommunication.waitAndReadData(serialPort);
            if (!Arrays.equals(protoclolAnswer, expectedSelectProtocolAnswer.getBytes())) {
                throw new InitializationException("Could not select protocol " + protocolName);
            }
        }
        catch(PortNotOpenException pnoe)
        {
            throw new PortNotOpenException("Could not use port " + serialPort.getSystemPortName() + " while initializing", pnoe);
        }
        catch(Exception e)
        {
            System.err.println("An exception occurred while initializing.");
        }
    }

    @Override
    public void reciveInitailizationAnswer() throws InitializationException, PortNotOpenException {
        try
        {
            byte[] initializeAnswer = serialCommunication.waitAndReadData(serialPort);
            if(!Arrays.equals("INIT...".getBytes(), initializeAnswer))
            {
                throw new InitializationException("Could not initialize connection in protocol " + protocolName + ". Expected answer in step one was 'INIT...' but received '" + new String(initializeAnswer) + "' instead");
            }
            initializeAnswer = serialCommunication.waitAndReadData(serialPort);
            if(!Arrays.equals("OK\n".getBytes(), new byte[]{initializeAnswer[0], initializeAnswer[1], initializeAnswer[3]}))
            {
                throw new InitializationException("Could not initialize connection in protocol " + protocolName + ". Expected answer in step two was 'OK' but received '" + new String(initializeAnswer) + "' instead");
            }
            initializeAnswer = DatatypeConverter.parseHexBinary(new String(initializeAnswer).substring(9,20).replace(" ", "").replace("\n", "")); // Most likely it's wrong XD
            extractAvailablePIDS(initializeAnswer);
        }
        catch(PortNotOpenException pnoe)
        {
            throw new PortNotOpenException("Could not use port " + serialPort.getSystemPortName() + " while initializing", pnoe);
        }
        catch(Exception e)
        {
            System.err.println("An exception occurred while initializing.");
        }

    }
}
