package CY.cymake.Domain.User;

import CY.cymake.Domain.User.Dto.RegisterReqDto;
import CY.cymake.Domain.User.Dto.UpdateReqDto;
import CY.cymake.Domain.User.Dto.UserInfoResDto;
import CY.cymake.Response.CommonBaseResult;
import CY.cymake.Response.CommonResult;
import CY.cymake.Response.GlobalResponseHandler;
import CY.cymake.Security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "회원 API")
@RequestMapping(value = "/v1/users")
@RestController
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;
    private final GlobalResponseHandler globalResponseHandler;

    /*
     * 회원가입 요청 시 RegisterReqDto에 유저가 입력한 정보를 받아옴. 해당 정보로 회원가입 로직 수행 후 결과 반환
     */
    @Operation(description = "회원가입")
    @PostMapping(value = "/register")
    public CommonBaseResult register(@Parameter(required = true, description = "회원 등록 정보") @RequestBody @Valid RegisterReqDto registerReqDto) {
        usersService.register(registerReqDto);
        //회원가입 성공~!
        return globalResponseHandler.SendSuccess();
    }
    /*
     * 회원 탈퇴 요청 시 @AuthenticationPrincipal 노테이션을 통해 인증 객체를 받아옴 -> 해당 정보로 유저를 찾아 테이블에서 지움
     */
    @Operation(description = "회원 탈퇴")
    @DeleteMapping(value = "/unregister")
    public CommonBaseResult delete(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        usersService.unregister(customUserDetails.getUser());
        return globalResponseHandler.SendSuccess();
    }

    /*
     * 유저 프로필 조회(내 프로필)
     */
    @Operation(description = "내 프로필 조회")
    @GetMapping(value = "/profile/{id}")
    public CommonResult<UserInfoResDto> getProfile(@PathVariable String id) {
        UserInfoResDto user = usersService.getProfile(id);
        return globalResponseHandler.SendSuccessAndContent(user);
    }

    /*
     * 유저 정보 수정
     */
    @Operation(description = "회원 정보 수정")
    @PutMapping(value = "/update")
    public CommonBaseResult updateProfile(@Parameter(required = true, description = "회원 정보 수정")@RequestBody @Valid UpdateReqDto updateReqDto){
        usersService.updateProfile(updateReqDto);
        return globalResponseHandler.SendSuccess();
    }
}
