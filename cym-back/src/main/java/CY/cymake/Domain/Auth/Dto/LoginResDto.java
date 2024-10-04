package CY.cymake.Domain.Auth.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/*
 * 로그인 처리 후 response 를 담을 객체
 */
@Data
@Schema(description = "회원 정보 반환")
public class LoginResDto {
    @NotBlank
    @Schema(description = "access token")
    private String accessToken;

    @NotBlank
    @Schema(description = "refresh token")
    private String refreshToken;

    @NotBlank
    @Schema(description = "id")
    private String id;

    @NotBlank
    @Schema(description = "username")
    private String username;

    @NotBlank
    @Schema(description = "email")
    private String email;

    @NotBlank
    @Schema(description = "role")
    private String role;

    @NotBlank
    @Schema(description = "유효기간")
    private String expireIn;

    @NotBlank
    @Schema(description = "용량")
    private double usage;

    @NotBlank
    @Schema(description = "요금제")
    private String plan;

    @Builder
    public LoginResDto(String accessToken, String refreshToken, String id, String username, String email, String role, String expireIn, double usage, String plan) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.expireIn = expireIn;
        this.usage = usage;
        this.plan = plan;
    }
}
