package CY.cymake.Exception;

import CY.cymake.Response.CommonBaseResult;
import CY.cymake.Response.GlobalResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/*
 * 전역 예외 처리 class
 * 각 예외를 컨트롤
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final GlobalResponseHandler globalResponseHandler;

    //default exception 핸들링
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public CommonBaseResult defaultException(Exception e) {
        log.error("[Default Exception Handle] ", e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
    //LoginFailedException 핸들링
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailedException.class)
    public CommonBaseResult loginFailedException(Exception e) {
        log.error("[LoginFailedException Handle] ", e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
    //GetProfileFailedException 핸들링
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GetProfileFailedException.class)
    public CommonBaseResult getProfileFailedException(Exception e) {
        log.error("[GetProfileFailedException Handle] ", e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
    //UpdateProfileFailedException 핸들링
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UpdateProfileFailedException.class)
    public CommonBaseResult updateProfileFailedException(Exception e) {
        log.error("[UpdateProfileFailedException Handle] ", e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    //RegisterFailedException -> 확인용 비밀번호 일치 X, 회사코드 일치 X(잘못 기재/등록되지 않은 회사코드)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RegisterFailedException.class)
    public CommonBaseResult RegisterFailedException(Exception e) {
        log.error("[RegisterFailedException Handle] " , e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    //UnregisterFailedException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnregisterFailedException.class)
    public CommonBaseResult unregisterFailedException(Exception e) {
        log.error("[UnregisterFailedException Handle] ", e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    //EmptyFileException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmptyFileException.class)
    public CommonBaseResult emptyFileException(Exception e) {
        log.error("[EmptyFileException Handle] ", e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    //FileUploadFailedException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileUploadFailedException.class)
    public CommonBaseResult fileUploadException(Exception e) {
        log.error("[FileUploadException Handle] ", e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    //UserNotFoundException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    public CommonBaseResult userNotFoundException(Exception e) {
        log.error("[FileUploadException Handle] ", e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
    //FileDeleteFailedException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileDeleteFailedException.class)
    public CommonBaseResult fileDeleteFailedException(Exception e) {
        log.error("[FileDeleteFailedException Handle] ", e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
    //FileUpdateFailedException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileUpdateFailedException.class)
    public CommonBaseResult fileUpdateFailedException(Exception e) {
        log.error("[FileUpdateFailedException Handle] ", e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
    //GetNewsException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GetNewsException.class)
    public CommonBaseResult getNewsException(Exception e) {
        log.error("[GetNewsException Handle] ", e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
    //SearchException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SearchException.class)
    public CommonBaseResult searchException(Exception e) {
        log.error("[SearchException Handle] ", e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    //ChatException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ChatException.class)
    public CommonBaseResult chatException(Exception e) {
        log.error("[ChatException Handle] ", e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

/*
refresh토큰 + redis 이용해 자동 로그인 + 로그아웃 구현 시 사용
    //UnauthorizedException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnauthorizedException.class)
    public CommonBaseResult unauthorizedException(Exception e) {
        log.error("[UnauthorizedExceptionHandle] ", e);
        return globalResponseHandler.SendFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

 */
}
