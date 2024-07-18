package CY.cymake.Exception;

public class EmptyFileException extends RuntimeException{
    public EmptyFileException(String msg, Throwable t) {  super(msg, t);  }
    public EmptyFileException(String msg) {  super(msg);  }
    public EmptyFileException(){  super();  }
}
