package ovh.spajste.yeti;

public class ThrottlePositionReadout extends Readout {

    public ThrottlePositionReadout() {
        name = "Throttle Position";
        value = 0;
        pid = 0x11;
        unit = "%";
    }

    public void calculateValue() {
        value = ((100/255)*readoutBuffer[0]);
    }

    public int getExpectedBytes() {
        return 1;
    }

    public int getExpectedMode() {
        return 0x41;
    }
}


