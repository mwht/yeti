package ovh.spajste.yeti;

import com.fazecast.jSerialComm.*;

import java.util.ArrayList;

/**
 * @author Michał Łukiański
 */
public class SerialCommunication {

    private SerialPort[] availablePorts;

    SerialCommunication() {
        availablePorts = SerialPort.getCommPorts();
    }

    public void rescanSerialPorts() {
        availablePorts = SerialPort.getCommPorts();
    }

    public String[] getSerialPortsSystemNames() {
        ArrayList<String> portNames = new ArrayList<>();
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


    public void openConnection(SerialPort port, int boud) throws PortNotOpenException {
        try
        {
            port.setBaudRate(boud);
            if(!port.openPort())
            {
                throw new PortNotOpenException("Cold not open port.");
            }
        }
        catch(NullPointerException e)
        {
            throw new PortNotOpenException("Could not open port.", e);
        }
    }

    public void closeConnection(SerialPort port)
    {
        if(port.isOpen())
            port.closePort();
    }

    public byte[] waitAndReadData(SerialPort port) throws PortNotOpenException {
        try
        {
            while (port.bytesAvailable() == 0) {}
            int amountOfBytesToRead = port.bytesAvailable();
            byte[] bytesToRead = new byte[amountOfBytesToRead];
            port.readBytes(bytesToRead, amountOfBytesToRead);
            return bytesToRead;
        }
        catch(NullPointerException e)
        {
            throw new PortNotOpenException("Port is not open.", e);
        }

    }

    public byte[] readData(SerialPort port) throws PortNotOpenException {
        try
        {
            int amountOfBytesToRead = port.bytesAvailable();
            byte[] bytesToRead = new byte[amountOfBytesToRead];
            port.readBytes(bytesToRead, amountOfBytesToRead);
            return bytesToRead;
        }
        catch(NullPointerException e)
        {
            throw new PortNotOpenException("Port is not open.", e);
        }

    }

    public void sendData(SerialPort port, byte[] dataToBeSend) throws PortNotOpenException, SerialSendDataException {
        try
        {
            if(port.writeBytes(dataToBeSend, dataToBeSend.length) == -1) {
                throw new SerialSendDataException("Could not write data.");
            }
        }
        catch(NullPointerException e)
        {
            throw new PortNotOpenException("Could not open port", e);
        }
    }
}
