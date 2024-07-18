package CY.cymake.Exception;

public class UnregisterFailedException extends RuntimeException{
    public UnregisterFailedException(String msg, Throwable t) {   super(msg, t);   }
    public UnregisterFailedException(String msg) {   super(msg);   }
    public UnregisterFailedException() {   super();   }
}
