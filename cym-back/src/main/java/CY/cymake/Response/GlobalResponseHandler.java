package CY.cymake.Response;

import org.springframework.stereotype.Component;

@Component
public class GlobalResponseHandler {
    //성공 시 성공 메시지/코드 + 전달 내용 send
    public <T> CommonResult<T> SendSuccessAndContent(T content) {
        CommonResult<T> result = new CommonResult<>();
        result.setContent(content);
        result.setCode(0);
        result.setMessage("success");
        return result;
    }
    //성공 시 성공 메시지/코드 send
    public CommonBaseResult SendSuccess () {
        CommonBaseResult result = new CommonBaseResult();
        result.setCode(0);
        result.setMessage("success");
        return result;
    }
    //실패 시 실패 메시지/코드 send
    public CommonBaseResult SendFailure (int code, String message) {
        CommonBaseResult result = new CommonBaseResult();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
