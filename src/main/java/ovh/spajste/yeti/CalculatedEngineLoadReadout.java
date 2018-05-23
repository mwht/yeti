package ovh.spajste.yeti;

public class CalculatedEngineLoadReadout extends Readout {

    public CalculatedEngineLoadReadout() {
        name = "Calculated Engine Load";
        value = 0;
        pid = 0x04;
        unit = "%";
    }

    public void calculateValue() {
        value = (readoutBuffer[0]/2.55);
    }

    public int getExpectedBytes() {
        return 1;
    }

    public int getExpectedMode() {
        return 0x41;
    }
}
