package ovh.spajste.yeti;

import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;

public abstract class Initialization {

    protected String protocolName;
    protected String protocolSelectSequence;
    protected SerialCommunication serialCommunication;
    protected SerialPort serialPort;
    protected String expectedSelectProtocolAnswer;

    public ArrayList<Byte> availablePIDS = new ArrayList<>();

    private byte[] initializeSequence = {0x01,0x00,0x0A}; // 0100\n
    private byte[] elmResetSequence = {0x41,0x54,0x20,0x5A,0x0A}; // AT Z\n
    private String elmIdentificator = "Unknown";

    public Initialization(SerialPort serialPort, SerialCommunication serialCommunication)
    {
        protocolName = "Unknown";
        protocolSelectSequence = "Unknown";
        expectedSelectProtocolAnswer = "Unknown";
        this.serialPort = serialPort;
        this.serialCommunication = serialCommunication;
    }

    public abstract void reciveProtocolSelectAnswer() throws InitializationException, PortNotOpenException;
    public abstract void reciveInitailizationAnswer() throws InitializationException, PortNotOpenException;

    public void extractAvailablePIDS(byte[] pids)
    {
            for(int i=0;i<4;i++) {
                for(int j=0;j<8;j++) {
                    if((pids[i] & (1<<(8-j))) != 0) {
                        if(!(i==0 && j==0)) availablePIDS.add((byte)((i*8)+j));
                    }
                }
            }
    }

    private void resetElm() throws SerialSendDataException, PortNotOpenException {
        serialCommunication.sendData(serialPort, elmResetSequence);
        byte[] elmAnswer = serialCommunication.waitAndReadData(serialPort);
        elmIdentificator = new String(elmAnswer);
    }

    void initialize() throws SerialSendDataException, PortNotOpenException, InitializationException {
        resetElm();
        serialCommunication.sendData(serialPort, protocolSelectSequence.getBytes());
        reciveProtocolSelectAnswer();
        serialCommunication.sendData(serialPort, initializeSequence);
        reciveInitailizationAnswer();
    }


}
