package ovh.spajste.yeti;

public class ThrottlePositionReadout extends Readout {

    public ThrottlePositionReadout() {
        name = "Throttle Position";
        value = 0;
        pid = 0x11;
        unit = "%";
        readoutBuffer = new byte[getExpectedBytes()];
    }

    public void calculateValue() {
        value = ((100.0/255.0)*readoutBuffer[0]);
        value = Math.round(value * 100) / 100;
    }

    public int getExpectedBytes() {
        return 1;
    }

    public int getExpectedMode() {
        return 0x41;
    }
}


