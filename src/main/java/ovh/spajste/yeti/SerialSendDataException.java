package ovh.spajste.yeti;

public class SerialSendDataException extends Exception{
    public SerialSendDataException()
    {
        super();
    }

    public SerialSendDataException(String message)
    {
        super(message);
    }

    public SerialSendDataException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SerialSendDataException(Throwable cause)
    {
        super(cause);
    }
}
