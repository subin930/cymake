package CY.cymake.Exception;

import java.io.IOException;

public class FileDeleteFailedException extends IOException {
    public FileDeleteFailedException(String msg, Throwable t) {  super(msg, t);  }
    public FileDeleteFailedException(String msg) {  super(msg);  }
    public FileDeleteFailedException(){  super();  }
}
