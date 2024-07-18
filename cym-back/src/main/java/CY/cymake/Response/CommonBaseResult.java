package CY.cymake.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "기본 응답 형식(응답 코드 + 응답 메시지)")
public class CommonBaseResult {
    @Schema(description = "응답 코드 / 0: 정상")
    private int code;

    @Schema(description = "응답 메시지")
    private String message;
}
