package CY.cymake.Exception;

public class GetNewsException extends Exception{
    public GetNewsException(String msg, Throwable t) {  super(msg, t);  }
    public GetNewsException(String msg) {  super(msg);  }
    public GetNewsException(){  super();  }
}
