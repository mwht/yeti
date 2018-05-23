package ovh.spajste.yeti;

public class LongTermFuelTrimBank1 extends Readout {

    public LongTermFuelTrimBank1() {
        name = "Long Term Furel Trim Bank 1";
        value = 0;
        pid = 0x07;
        unit = "%";
    }

    public void calculateValue() {
        value = ((readoutBuffer[0]/1.28)-100);
    }

    public int getExpectedBytes() {
        return 1;
    }

    public int getExpectedMode() {
        return 0x41;
    }
}