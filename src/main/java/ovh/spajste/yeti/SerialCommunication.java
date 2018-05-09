package ovh.spajste.yeti;

import com.fazecast.jSerialComm.*;

import java.util.ArrayList;

/**
 * @author Michał Łukiański
 */
public class SerialCommunication {

    private SerialPort[] availablePorts = null;

    SerialCommunication() {
        availablePorts = SerialPort.getCommPorts();
    }

    public void rescanSerialPorts() {
        availablePorts = SerialPort.getCommPorts();
    }

    public String[] getSerialPortsSystemNames() {
        ArrayList<String> portNames = new ArrayList<String>();
        for (SerialPort port : availablePorts) {
            portNames.add(port.getSystemPortName());
        }
        return portNames.toArray(new String[0]);
    }

    public SerialPort selectPort(String portName)
    {
        for(SerialPort port: availablePorts)
        {
            if (portName.equals(port.getSystemPortName()))
                return port;
        }
        return null;
    }


    public void openConnection(SerialPort port, int boud)
    {
        port.setBaudRate(boud);
        port.openPort();
    }

    public void closeConnection(SerialPort port)
    {
        if(port.isOpen())
            port.closePort();
    }

    public byte[] readData(SerialPort port, long amountOfBytesToRead)
    {
        if(!port.isOpen())
        {
            /*
                TODO: Throw exception about not open port

             */
        }
        byte[] bytesToRead = new byte[(int)amountOfBytesToRead];
        port.readBytes(bytesToRead, amountOfBytesToRead);
        return bytesToRead;

    }

    public void sendData(SerialPort port, byte[] dataToBeSend,long amountOfBytesToSend)
    {
        if(port.writeBytes(dataToBeSend, amountOfBytesToSend) == -1)
        {
            /*
                TODO: Throw exception about not sucessfuly send data
             */
        }
    }
}
