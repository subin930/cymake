/*
refresh토큰 + redis 이용해 자동 로그인 + 로그아웃 구현 시 사용하는 dto
package CY.cymake.Domain.Auth.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
//
// reissue 또는 logout 요청 시 받는 정보 Dto
//
@Data
@Schema(description = "Access토큰, Refresh토큰 담는 Dto")
public class TokenDto {
    @Schema(description = "access 토큰")
    @NotBlank
    private final String accessToken;

    @Schema(description = "refresh 토큰")
    @NotBlank
    private final String refreshToken;
}
*/