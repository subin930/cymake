package CY.cymake.Domain.User.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(description = "유저 정보 반환")
@Builder
public class UserInfoResDto {
    @NotBlank
    @Schema(description = "아이디")
    String id;

    @NotBlank
    @Schema(description = "이름")
    String username;

    @NotBlank
    @Schema(description = "이메일")
    String email;
}
