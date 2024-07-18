package CY.cymake.Domain.User.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "회원 정보 수정 Dto")
public class UpdateReqDto {
    @NotBlank
    @Schema(description = "아이디", example = "gildong123")
    String id;

    @NotBlank
    @Schema(description = "이름", example = "길똥씨")
    String username;

    @NotBlank
    @Schema(description = "비밀번호", example = "gildong123")
    String password;

    @NotBlank
    @Schema(description = "이메일", example = "gildong123@gmail.com")
    String email;
}
