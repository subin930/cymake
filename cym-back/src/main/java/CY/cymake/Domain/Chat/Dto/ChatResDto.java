package CY.cymake.Domain.Chat.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "챗봇 검색 응답 Dto")
public class ChatResDto {
    @Schema(description = "대답")
    String response;

    @Schema(description = "세션ID")
    String sessionId;
}
