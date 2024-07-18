package CY.cymake.Domain.Auth.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/*
 * 로그인 요청 시(폼 입력 후 확인 눌렀을 때)
 * 폼을 통해 유저가 입력한 정보를 담을 객체
 */
@Data
@Schema(description = "로그인 요청 Dto")
public class LoginReqDto {
    @NotBlank(message = "회사 코드 입력은 필수입니다.")
    @Schema(description = "회사 코드", example = "CYGLOBAL")
    private String company_code;

    @NotBlank(message = "아이디 입력은 필수입니다.")
    @Schema(description = "아이디", example = "gildong123")
    private String id;

    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    @Schema(description = "비밀번호", example = "password123!")
    private String password;
}
