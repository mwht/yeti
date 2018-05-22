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

    private final String ERROR_CODE_REGEX = "^7F 12$";

    /**
     * Constructor for Readout.
     */
    public Readout() {
        name = "Unknown";
        value = 0;
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

    public byte[] getBytes() {
        // TODO
        return null;
    }

    public double getValue() {
        /*
        TODO: fetch getExpectedBytes() from ELM and calculate it's value
              or
              use ELM class for delivering the data
         */

        calculateValue();
        return value;
    }
}
