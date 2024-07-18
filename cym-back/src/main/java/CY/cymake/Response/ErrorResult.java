package CY.cymake.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {
    @Schema(description = "응답 코드")
    private String code;

    @Schema(description = "응답 메시지")
    private String message;
}