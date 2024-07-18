package CY.cymake.Exception;

public class UpdateProfileFailedException extends RuntimeException{
    public UpdateProfileFailedException(String msg, Throwable t) {   super(msg, t);   }
    public UpdateProfileFailedException(String msg) {   super(msg);   }
    public UpdateProfileFailedException() {   super();   }
}
