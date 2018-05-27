package ovh.spajste.yeti;

public class IntakeMainfoldAbsolutePressureReadout extends Readout {

    public IntakeMainfoldAbsolutePressureReadout() {
        name = "Intake Mainfold Absolute Pressure";
        value = 0;
        pid = 0x0B;
        unit = "kPa";
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

