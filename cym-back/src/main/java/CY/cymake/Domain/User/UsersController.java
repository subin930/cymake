package CY.cymake.Domain.User;

import CY.cymake.Domain.User.Dto.RegisterReqDto;
import CY.cymake.Domain.User.Dto.UpdatePWReqDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@Tag(name = "User", description = "회원 API")
@RequestMapping(value = "/v1/users")
@RestController
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;
    private final GlobalResponseHandler globalResponseHandler;
    private int number;

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
    * 이메일 인증 (전송)
     */
    @PostMapping("/mailSend")
    public HashMap<String, Object> mailSend(@Parameter(description = "이메일 인증번호 요청") @RequestParam String mail) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            number = usersService.sendMail(mail);
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e){
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }
        return map;
    }

    @GetMapping("/mailCheck")
    public ResponseEntity<?> mailCheck(@Parameter(description = "이메일 인증 확인") @RequestParam String userNumber) {

        boolean isMatch = userNumber.equals(String.valueOf(number));

        return ResponseEntity.ok(isMatch);
    }


    /*
     * 회원 탈퇴 요청 시 @AuthenticationPrincipal 노테이션을 통해 인증 객체를 받아옴 -> 해당 정보로 유저를 찾아 테이블에서 지움
     */
    @Operation(description = "회원 탈퇴")
    @DeleteMapping(value = "/unregister")
    public CommonBaseResult delete(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
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
     * 유저 정보 수정(pw)
     */
    @Operation(description = "회원 정보 수정")
    @PutMapping(value = "/updatePW")
    public CommonBaseResult updatePWProfile(@Parameter(required = true, description = "회원 정보 수정")@RequestBody @Valid UpdatePWReqDto updatePWReqDto, @AuthenticationPrincipal CustomUserDetails user){
        usersService.updatePWProfile(user.getUser(), updatePWReqDto);
        return globalResponseHandler.SendSuccess();
    }
    /*
     * 유저 정보 수정(pw)
     */
    @Operation(description = "회원 정보 수정")
    @PutMapping(value = "/update")
    public CommonBaseResult updateProfile(@Parameter(required = true, description = "회원 정보 수정")@RequestParam(value = "email") String email, @RequestParam(value = "plan") String plan, @AuthenticationPrincipal CustomUserDetails user){
        usersService.updateProfile(user.getUser(), email, plan);
        return globalResponseHandler.SendSuccess();
    }


}
