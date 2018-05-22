package ovh.spajste.yeti;

public class RPMReadout extends Readout {

    public RPMReadout() {
        name = "RPM";
        value = 0;
        pid = 0x0C;
    }

    public void calculateValue() {

    }

    public int getExpectedBytes() {
        return 2;
    }

    public int getExpectedMode() {
        return 4;
    }
}
