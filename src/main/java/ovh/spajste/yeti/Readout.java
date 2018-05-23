package ovh.spajste.yeti;

/**
 * Readout is abstract class representing generic readout from OBD-II/ELM interface.
 *
 * @author Sebastian Madejski
 */
public abstract class Readout {
    protected String name;
    protected double value;
    protected byte pid;
    protected String unit;
    protected byte[] readoutBuffer;
    protected boolean active = false;

    private final String ERROR_CODE_REGEX = "^7F 12$";

    /**
     * Constructor for Readout.
     */
    public Readout() {
        name = "Unknown";
        value = 0;
        unit = "?";
        active = false;
    }

    /**
     * Constructor for Readout, setting the name parameter.
     *
     * @param name Readout name
     */
    public Readout(String name) {
        this.name = name;
        value = 0;
    }

    /**
     *
     */
    public abstract void calculateValue();
    public abstract int getExpectedBytes();
    public abstract int getExpectedMode();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReadoutBuffer(byte[] readoutBuffer) {
        this.readoutBuffer = readoutBuffer;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getValue() {
        if(readoutBuffer.length != getExpectedBytes()) {
            // TODO: throw invalid readout exception
        }
        calculateValue();
        return value;
    }

    public byte getPid() {
        return pid;
    }

    public void setActive(boolean status)
    {
        active = status;
    }

    public boolean getActive()
    {
        return active;
    }
}
