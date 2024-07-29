package CY.cymake.Domain.Auth;

import CY.cymake.Domain.Auth.Dto.CustomUserInfoDto;
import CY.cymake.Domain.Auth.Dto.LoginReqDto;
import CY.cymake.Domain.Auth.Dto.LoginResDto;
import CY.cymake.Entity.UsersEntity;
import CY.cymake.Exception.LoginFailedException;
import CY.cymake.Repository.CompanyRepository;
import CY.cymake.Repository.UsersRepository;
import CY.cymake.Security.CustomUserDetailService;
import CY.cymake.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;
    private final CustomUserDetailService customUserDetailService;
    //private final RedisUtil redisUtil;
    /*
     * 로그인 로직 처리
     */
    @Transactional(readOnly = true)
    public LoginResDto login(LoginReqDto loginReqDto) {
        String id = loginReqDto.getId();
        String password = loginReqDto.getPassword();

        //1. 회사코드 확인: 회사코드가 존재하는지 & 유저의 회사코드와 입력한 회사코드가 일치하는지 확인
        //유저가 입력한 회사 코드가 존재하는지
        if(companyRepository.findByCode(loginReqDto.getCompanyCode()).isEmpty()) {
            throw new LoginFailedException("회사코드가 존재하지 않습니다.");
        }

        //2. username으로 유저 검색
        Optional<UsersEntity> user = usersRepository.findById(id);
        if(user.isEmpty()) {
            //해당 아이디를 가진 유저가 존재하지 않는 경우
            throw new LoginFailedException("아이디가 존재하지 않습니다.");
        }
        UsersEntity siteUser = user.get();

        //3. 비밀번호 확인: 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 null 반환
        if(!encoder.matches(password, siteUser.getPassword())) {
            throw new LoginFailedException("비밀번호가 일치하지 않습니다.");
        }


        //db에 저장된 유저의 회사코드와 유저가 폼을 통해 입력한 회사코드가 일치하는지
        if(!siteUser.getCompanyCode().getCode().equals(loginReqDto.getCompanyCode())) {
            throw new LoginFailedException("회사코드가 일치하지 않습니다.");
        }

        CustomUserInfoDto info = modelMapper.map(siteUser, CustomUserInfoDto.class);

        String accessToken = jwtUtil.createAccessToken(info);
        String refreshToken = jwtUtil.createRefreshToken(info);

        return LoginResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(info.getId())
                .username(info.getUsername())
                .email(info.getEmail())
                .role(info.getRole().name())
                .expireIn(jwtUtil.getExpireIn()) //access token의 만료 기간
                .build();
    }
/*
refresh토큰 + redis 이용해 자동 로그인 + 로그아웃 구현 시 사용
    @Transactional
    public LoginResDto reissue(String requestAccessToken, String requestRefreshToken) {
        if(!jwtUtil.validateToken(requestRefreshToken)){
            throw new UnauthorizedException("유효하지 않은 Refresh 토큰입니다.");
        }
        String username = jwtUtil.getUsername(requestRefreshToken);
        //토큰 재발급
        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
        if(userDetails != null) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        Optional<UsersEntity> user = usersRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("토큰 정보와 일치하는 이름(ID)가 존재하지 않습니다.");
        }
        UsersEntity siteUser = user.get();
        CustomUserInfoDto info = modelMapper.map(siteUser, CustomUserInfoDto.class);
        String accessToken = jwtUtil.createAccessToken(info);
        String refreshToken = jwtUtil.createRefreshToken(info);
        return LoginResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(info.getId())
                .email(info.getEmail())
                .role(info.getRole().name())
                .expireIn(jwtUtil.getExpireIn()) //access token의 만료 기간
                .build();
    }
    public void logout(String accessToken, String refreshToken) {
        redisUtil.setBlackList(accessToken, "accessToken", 1000 * 60 * 30);
        redisUtil.setBlackList(refreshToken, "refreshToken", 1000 * 60 * 60 * 24 * 7);
    }

 */
}
