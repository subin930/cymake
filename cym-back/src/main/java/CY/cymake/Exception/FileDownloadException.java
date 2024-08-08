package CY.cymake.Exception;

public class FileDownloadException extends Exception{
    public FileDownloadException(String msg, Throwable t) {  super(msg, t);  }
    public FileDownloadException(String msg) {  super(msg);  }
    public FileDownloadException(){  super();  }
}
