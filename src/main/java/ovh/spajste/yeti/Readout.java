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
     * Calculate the value from given readout buffer.
     */
    public abstract void calculateValue();
    
    /**
     * Gets the number of bytes expected by readout.
     * @return number of bytes expected by readout
     */
    public abstract int getExpectedBytes();
    
    /**
     * Gets the number of mode expectedy by readout
     * @return number of mode expected by readout
     */
    public abstract int getExpectedMode();

    /**
     * Gets the name of readout.
     * @return name of readout
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of readout.
     * @param name name of readout
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the readout buffer. Used internally in {@link ELMInterface}.
     * @param readoutBuffer buffer
     */
    public void setReadoutBuffer(byte[] readoutBuffer) {
        this.readoutBuffer = readoutBuffer;
    }

    /**
     * Gets the unit which the readout returns the value in.
     * @return unit of readout
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit which the readout returns the value in.
     * @param unit unit of readout
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Gets the value of readout. Throws {@link InvalidReadoutException}
     * 
     * @return value of readout
     * @throws InvalidReadoutException
     */
    public double getValue() throws InvalidReadoutException {
        if(readoutBuffer.length != getExpectedBytes()) {
            throw new InvalidReadoutException("Invalid number of bytes (readout type mismatch?)");
        }
        calculateValue();
        return value;
    }

    /**
     * Gets the PID of readout itself.
     * 
     * @return readout's PID
     */
    public byte getPid() {
        return pid;
    }

    /**
     * Sets whether readout should be polled by {@link ELMInterface}.
     * @param active activity status of readout
     */
    public void setActive(boolean status)
    {
        active = status;
    }

    /**
     * Gets whether readout should be polled by {@link ELMInterface}.
     * @return activity status of readout
     */
    public boolean getActive()
    {
        return active;
    }
}
