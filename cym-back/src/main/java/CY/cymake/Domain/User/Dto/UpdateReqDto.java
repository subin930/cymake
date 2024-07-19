package CY.cymake.Domain.User.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "회원 정보 수정 Dto")
public class UpdateReqDto {
    @NotBlank(message = "기존 비밀번호 입력은 필수입니다.")
    @Schema(description = "기존 비밀번호", example = "password123!")
    String originalPassword;

    @NotBlank(message = "새로운 비밀번호 입력은 필수입니다.")
    @Schema(description = "새로운 비밀번호", example = "password1234!")
    String newPassword;

    @NotBlank(message = "새로운 비밀번호 확인 입력은 필수입니다.")
    @Schema(description = "새로운 비밀번호 확인", example = "password1234!")
    String newPasswordCheck;
}
