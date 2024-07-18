package CY.cymake.Exception;

public class GetProfileFailedException extends RuntimeException{
    public GetProfileFailedException(String msg, Throwable t) {  super(msg, t);  }
    public GetProfileFailedException(String msg) {  super(msg);  }
    public GetProfileFailedException(){  super();  }
}
