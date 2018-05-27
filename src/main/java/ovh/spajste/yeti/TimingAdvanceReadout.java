package ovh.spajste.yeti;

public class TimingAdvanceReadout extends Readout {

    public TimingAdvanceReadout() {
        name = "Timing Advance";
        value = 0;
        pid = 0x0E;
        unit = "°";
        readoutBuffer = new byte[getExpectedBytes()];
    }

    public void calculateValue() {
        value = ((readoutBuffer[0]/2)-64);
    }

    public int getExpectedBytes() {
        return 1;
    }

    public int getExpectedMode() {
        return 0x41;
    }
}


