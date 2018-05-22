package ovh.spajste.yeti;

public class PortNotOpenException extends Exception {
    public PortNotOpenException()
    {
        super();
    }

    public PortNotOpenException(String message)
    {
        super(message);
    }

    public PortNotOpenException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public PortNotOpenException(Throwable cause)
    {
        super(cause);
    }
}
