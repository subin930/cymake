package CY.cymake.Exception;

public class ChatException extends RuntimeException{
    public ChatException(String msg, Throwable t) {  super(msg, t);  }
    public ChatException(String msg) {  super(msg);  }
    public ChatException(){  super();  }
}
