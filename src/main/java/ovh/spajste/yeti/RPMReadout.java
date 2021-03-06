package ovh.spajste.yeti;

public class RPMReadout extends Readout {

    public RPMReadout() {
        name = "RPM";
        value = 0;
        pid = 0x0C;
        unit = "rpm";
        readoutBuffer = new byte[getExpectedBytes()];
    }

    public void calculateValue() {
        value = ((256*readoutBuffer[0])+readoutBuffer[1])/4;
    }

    public int getExpectedBytes() {
        return 2;
    }

    public int getExpectedMode() {
        return 0x41;
    }
}
