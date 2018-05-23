package ovh.spajste.yeti;

public class IntakeAirTemperatureReadout extends Readout {

    public IntakeAirTemperatureReadout() {
        name = "Intake Air Temperature";
        value = 0;
        pid = 0x0F;
        unit = "°C";
    }

    public void calculateValue() {
        value = (readoutBuffer[0]-40);
    }

    public int getExpectedBytes() {
        return 1;
    }

    public int getExpectedMode() {
        return 0x41;
    }
}


