package ovh.spajste.yeti;

public class VehicleSpeedReadout extends Readout {

    public VehicleSpeedReadout() {
        name = "Vehicle Speed";
        value = 0;
        pid = 0x0D;
        unit = "km/h";
        readoutBuffer = new byte[getExpectedBytes()];
    }

    public void calculateValue() {
        value = (readoutBuffer[0]);
    }

    public int getExpectedBytes() {
        return 1;
    }

    public int getExpectedMode() {
        return 0x41;
    }
}


