package CY.cymake.Domain.User;

import CY.cymake.Domain.Auth.Dto.CustomUserInfoDto;
import CY.cymake.Domain.User.Dto.UpdateReqDto;
import CY.cymake.Domain.User.Dto.UserInfoResDto;
import CY.cymake.Entity.CompanyEntity;
import CY.cymake.Entity.UsersEntity;
import CY.cymake.Exception.*;
import CY.cymake.Repository.CompanyRepository;
import CY.cymake.Repository.UsersRepository;
import CY.cymake.Domain.User.Dto.RegisterReqDto;
import CY.cymake.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder encoder;

    /*
     * 회원가입 처리 로직
     */
    @Transactional
    public void register(RegisterReqDto registerReqDto) {
        //회원가입 - id, email, company_code, password1, password2 입력받음

        //1. 아이디 중복 확인
        if(usersRepository.findById(registerReqDto.getId()).isPresent()){
            //중복
            throw new RegisterFailedException("중복되는 아이디입니다.");
        }
        //2. email 중복 확인
        if(usersRepository.findByEmail(registerReqDto.getEmail()).isPresent()) {
            //중복
            throw new RegisterFailedException("중복되는 email입니다.");
        }
        //3. password 일치 확인
        if(!registerReqDto.getPassword1().equals(registerReqDto.getPassword2())){
            //비밀번호 일치 X
            throw new RegisterFailedException("비밀번호가 일치하지 않습니다.");
        }
        //4. 회사 코드 일치 확인
        Optional<CompanyEntity> company = companyRepository.findByCode(registerReqDto.getCompany_code());
        if(company.isEmpty()) {
            throw new RegisterFailedException("회사코드가 일치하지 않습니다.");
        }
        //5. 비밀번호 암호화
        registerReqDto.setPassword1(encoder.encode(registerReqDto.getPassword1()));
        //6. 회원가입 진행
        usersRepository.save(registerReqDto.toUsersEntity(company.get()));
    }

    /*
     * 회원탈퇴 처리 로직
     */
    @Transactional
    public void unregister(CustomUserInfoDto user) {
        //권한 정보에 들어있는데 유저 정보 이용해 회원 탈퇴 처리
        String id = user.getId();
        Optional<UsersEntity> siteUser = usersRepository.findById(id);
        if(siteUser.isEmpty()) {
            throw new UnregisterFailedException("회원탈퇴에 실패하였습니다.");
        }
        usersRepository.delete(siteUser.get());
    }
    /*
     * 내 프로필 조회 로직
     */
    @Transactional
    public UserInfoResDto getProfile(String id) {
        Optional<UsersEntity> siteUser = usersRepository.findById(id);
        if(siteUser.isEmpty()) {
            throw new GetProfileFailedException("프로필 조회에 실패하였습니다.");
        }
        UsersEntity user = siteUser.get();
        return UserInfoResDto.builder()
                .id(id)
                .company_code(user.getCompany_code().getCode())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    /*
     * 회원 정보 수정 로직
     */
    public void updateProfile(UpdateReqDto updateReqDto) {
        Optional<UsersEntity> siteUser = usersRepository.findById(updateReqDto.getId());
        if(siteUser.isEmpty()) {
            throw new UpdateProfileFailedException("회원 정보 수정에 실패하였습니다: 해당 회원을 찾을 수 없음.");
        }
        UsersEntity user = siteUser.get();

        //비밀번호 암호화
        updateReqDto.setPassword(encoder.encode(updateReqDto.getPassword()));

        //수정
        user.updateProfile(updateReqDto.getPassword(), updateReqDto.getEmail());
        usersRepository.save(user);
    }
}
