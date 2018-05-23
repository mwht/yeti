package ovh.spajste.yeti;

public class InvalidReadoutException extends Exception {
    public InvalidReadoutException()
    {
        super();
    }

    public InvalidReadoutException(String message)
    {
        super(message);
    }

    public InvalidReadoutException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidReadoutException(Throwable cause)
    {
        super(cause);
    }
}
