package CY.cymake.Exception;

public class SearchException extends Exception{
    public SearchException(String msg, Throwable t) {   super(msg, t);   }
    public SearchException(String msg) {   super(msg);   }
    public SearchException() {   super();   }
}
