package CY.cymake.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Schema(description = "응답 형식(전달 내용 + 응답 코드 + 응답 메시지)")
public class CommonResult<T> extends CommonBaseResult{
    private T content;
}
