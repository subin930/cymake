package CY.cymake.Domain.Chat.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "챗봇 검색 Dto")
public class ChatReqDto {
    @NotBlank
    @Schema(description = "회사코드")
    String companyCode;

    @NotBlank(message = "질문 입력은 필수입니다.")
    @Schema(description = "질문")
    String question;

    @Schema(description = "세션ID")
    String sessionId;

    @Schema(description = "파일 url")
    String url;
}
