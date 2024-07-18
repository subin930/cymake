package CY.cymake.Domain.User.Dto;

import CY.cymake.Entity.CompanyEntity;
import CY.cymake.Entity.Role;
import CY.cymake.Entity.UsersEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "회원가입 요청 Dto")
public class RegisterReqDto {
    @NotBlank(message = "아이디 입력은 필수입니다.")
    @Schema(description = "아이디", example = "gildong123")
    private String id;

    @NotBlank(message = "이름 입력은 필수입니다.")
    @Schema(description = "이름", example = "길똥씨")
    private String username;

    @NotBlank(message = "회사코드 입력은 필수입니다.")
    @Schema(description = "회사코드", example = "CYGLOBAL")
    private String company_code;

    @NotBlank(message = "이메일 입력은 필수입니다.")
    @Schema(description = "이메일", example = "gildong@gmail.com")
    private String email;

    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    @Schema(description = "비밀번호", example = "password123!")
    private String password1;

    @NotBlank(message = "확인용 비밀번호 입력은 필수입니다.")
    @Schema(description = "비밀번호 확인", example = "password123!")
    private String password2;

    public UsersEntity toUsersEntity(CompanyEntity company) {
        return UsersEntity.builder()
                .id(id)
                .username(username)
                .password(password1)
                .email(email)
                .company_code(company)
                .role(Role.USER)
                .build();
    }
}
