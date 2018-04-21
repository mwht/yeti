package ovh.spajste.yeti;

import com.fazecast.jSerialComm.*;

import java.util.ArrayList;

/**
 *
 */
public class SerialCommunication {

    private SerialPort[] availablePorts = null;
    SerialCommunication()
    {
        availablePorts = SerialPort.getCommPorts();
    }

    public void rescanSerialPorts()
    {
        availablePorts = SerialPort.getCommPorts();
    }

    public String[] getSerialPortsSystemNames()
    {
        ArrayList<String> portNames = new ArrayList<String>();
        for (SerialPort port : availablePorts) {
            portNames.add(port.getSystemPortName());
        }
        return portNames.toArray(new String[0]);
    }

}
