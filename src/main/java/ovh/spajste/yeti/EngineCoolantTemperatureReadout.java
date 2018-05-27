package ovh.spajste.yeti;

public class EngineCoolantTemperatureReadout extends Readout {

    public EngineCoolantTemperatureReadout() {
        name = "Engine Coolant Temperature";
        value = 0;
        pid = 0x05;
        unit = "°C";
        readoutBuffer = new byte[getExpectedBytes()];
    }

    public void calculateValue() {
        value = (readoutBuffer[0]-40);
    }

    public int getExpectedBytes() {
        return 1;
    }

    public int getExpectedMode() {
        return 0x41;
    }
}
