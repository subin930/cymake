package CY.cymake.Exception;

public class FileUpdateFailedException extends RuntimeException{
    public FileUpdateFailedException(String msg, Throwable t) {  super(msg, t);  }
    public FileUpdateFailedException(String msg) {  super(msg);  }
    public FileUpdateFailedException(){  super();  }
}
