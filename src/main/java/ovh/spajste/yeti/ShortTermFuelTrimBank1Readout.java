package ovh.spajste.yeti;

public class ShortTermFuelTrimBank1Readout extends Readout {

    public ShortTermFuelTrimBank1Readout() {
        name = "Short Term Fuel Trim Bank 1";
        value = 0;
        pid = 0x06;
        unit = "%";
        readoutBuffer = new byte[getExpectedBytes()];
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
