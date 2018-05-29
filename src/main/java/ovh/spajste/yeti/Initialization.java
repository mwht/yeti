package ovh.spajste.yeti;

import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;

public abstract class Initialization {

    protected String protocolName;
    protected String protocolSelectSequence;

    public ArrayList<Byte> availablePIDS = new ArrayList<>();

    private byte[] initializeSequence = {0x01,0x00,0x0A}; // 0100\n
    private byte[] elmResetSequence = {0x41,0x54,0x20,0x5A,0x0A}; // AT Z\n
    private String elmIdentificator = "Unknown";
    private SerialCommunication serialCommunication;
    private SerialPort serialPort;
    static private byte[] listOfAllPIDS = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,0x10,0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18,0x19,0x1A,0x1B,0x1C,0x1D,0x1E,0x1F,0x20};

    Initialization(SerialPort serialPort, SerialCommunication serialCommunication)
    {
        protocolName = "Unknown";
        protocolSelectSequence = "Unknown";
        this.serialPort = serialPort;
        this.serialCommunication = serialCommunication;
    }

    public abstract void reciveProtocolSelectAnswer() throws InitializationException;
    public abstract void reciveInitailizationAnswer() throws InitializationException;

    private void extractAvailablePIDS(byte[] pids)
    {
            int j = 0;
            for (int i = 0; i < 32; i++)
            {
                if(j % 8 == 0 && j != 0)
                {
                    j++;
                }
                if((pids[j] & (1<<i)) == 1)
                {
                    availablePIDS.add(listOfAllPIDS[i]);
                }
            }
    }

    private void resetElm() throws SerialSendDataException, PortNotOpenException {
        serialCommunication.sendData(serialPort, elmResetSequence);
        byte[] elmAnswer = serialCommunication.waitAndReadData(serialPort);
        elmIdentificator = new String(elmAnswer);
    }

    void initializeProtocol() throws SerialSendDataException, PortNotOpenException, InitializationException {
        resetElm();
        serialCommunication.sendData(serialPort, protocolSelectSequence.getBytes());
        reciveProtocolSelectAnswer();
        serialCommunication.sendData(serialPort, initializeSequence);
        reciveInitailizationAnswer();
    }


}
