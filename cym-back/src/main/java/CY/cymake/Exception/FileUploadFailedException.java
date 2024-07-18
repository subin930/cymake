package CY.cymake.Exception;

import java.io.IOException;

public class FileUploadFailedException extends IOException {
    public FileUploadFailedException(String msg, Throwable t) {  super(msg, t);  }
    public FileUploadFailedException(String msg) {  super(msg);  }
    public FileUploadFailedException(){  super();  }
}
