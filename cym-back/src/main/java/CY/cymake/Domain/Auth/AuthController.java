package CY.cymake.Domain.Auth;

import CY.cymake.Domain.Auth.Dto.LoginReqDto;
import CY.cymake.Domain.Auth.Dto.LoginResDto;
import CY.cymake.Response.CommonResult;
import CY.cymake.Response.GlobalResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Tag(name = "Auth", description = "권한 API")
public class AuthController {
    private final AuthService authService;
    private final GlobalResponseHandler globalResponseHandler;

    /*
     * 로그인 요청 시 LoginReqDto를 통해 정보를 받아와 로그인 로직 수행 후 결과를 response에 담아서 보냄
     */
    @Operation(description = "로그인")
    @PostMapping("/login")
    public CommonResult<LoginResDto> login(@Parameter(required = true, description = "로그인 요청 정보") @Valid @RequestBody LoginReqDto loginReqDto) {
        LoginResDto response = this.authService.login(loginReqDto);
        return globalResponseHandler.SendSuccessAndContent(response);
    }
/*
 * refresh 토큰 + redis 이용해 자동 로그인 구현하는 코드 -> 추후 개발 에정
    //
    // Access Token 만료 시 토큰(AccessToken, RefreshToken) 재발급해주는 메서드
    //
    @Operation
    @PostMapping("/reissue")
    public CommonResult<LoginResDto> reissue(@Valid @RequestBody TokenDto tokenDto) {
        LoginResDto response = authService.reissue(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        return globalResponseHandler.SendSuccessAndContent(response);
    }

    //
    // 로그아웃 요청 시 토큰을 받아 redis의 BlackList에 저장하는 메서드
    //
    @Operation
    @DeleteMapping("/logout")
    public CommonBaseResult logout(@Valid @RequestBody TokenDto tokenDto) {
        authService.logout(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        return globalResponseHandler.SendSuccess();
    }
*/


}
