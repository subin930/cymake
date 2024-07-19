package CY.cymake.Domain.Auth.Dto;


import CY.cymake.Entity.CompanyEntity;
import CY.cymake.Entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/*
 * JWT 생성 시에 임시로 들고 있는 유저 정보를 담을 객체
 * 이 정보를 이용해 body 에 넣어서 보냄
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "JWT 생성에 필요한 유저 정보 Dto")
public class CustomUserInfoDto {
    @Schema(description = "user_pk")
    private Long user_pk;

    @Schema(description = "id")
    private String id;

    @Schema(description = "companyCode")
    private CompanyEntity companyCode;

    @Schema(description = "password")
    private String password;

    @Schema(description = "email")
    private String email;

    @Schema(description = "role")
    private Role role;
}
