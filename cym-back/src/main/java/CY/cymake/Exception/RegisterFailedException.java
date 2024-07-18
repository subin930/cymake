package CY.cymake.Exception;

/*
 * 회원가입 시:
 * 1. 확인용 비밀번호 오류
 * 2. 회사 코드 오류
 * 위 경우에 RegisterFailedException 으로 처리
 */
public class RegisterFailedException extends RuntimeException{
    public RegisterFailedException(String msg, Throwable t) {   super(msg, t);   }
    public RegisterFailedException(String msg) {   super(msg);   }
    public RegisterFailedException() {   super();   }
}
