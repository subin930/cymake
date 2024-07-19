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
        //회원가입 - id, email, companyCode, password1, password2 입력받음

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
        if(!registerReqDto.getPassword().equals(registerReqDto.getPasswordCheck())){
            //비밀번호 일치 X
            throw new RegisterFailedException("비밀번호가 일치하지 않습니다.");
        }
        //4. 회사 코드 일치 확인
        Optional<CompanyEntity> company = companyRepository.findByCode(registerReqDto.getCompanyCode());
        if(company.isEmpty()) {
            throw new RegisterFailedException("회사코드가 일치하지 않습니다.");
        }
        //5. 비밀번호 암호화
        registerReqDto.setPassword(encoder.encode(registerReqDto.getPassword()));
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
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    /*
     * 회원 정보 수정 로직
     */
    public void updateProfile(CustomUserInfoDto customUserInfoDto, UpdateReqDto updateReqDto) {
        Optional<UsersEntity> siteUser = usersRepository.findById(customUserInfoDto.getId());
        if(siteUser.isEmpty()) {
            throw new UpdateProfileFailedException("회원 정보 수정에 실패하였습니다: 해당 회원을 찾을 수 없음.");
        }
        UsersEntity user = siteUser.get();
        //기존 비밀번호 확인
        if(!encoder.matches(updateReqDto.getOriginalPassword(), user.getPassword())) {
            //비밀번호 일치 X
            throw new UpdateProfileFailedException("기존 비밀번호가 일치하지 않습니다.");
        }

        //비밀번호 확인
        //2. 기존 비밀번호와 새로운 비밀번호가 같으면 실패
        if(updateReqDto.getOriginalPassword().equals(updateReqDto.getNewPassword())) {
            throw new UpdateProfileFailedException("기존 비밀번호와 새로운 비밀번호가 같습니다.");
        }
        //3. 새로운 비밀번호와 비밀번호 확인이 다르면 실패
        if(!updateReqDto.getNewPassword().equals(updateReqDto.getNewPasswordCheck())) {
            throw new UpdateProfileFailedException("비밀번호가 일치하지 않습니다.");
        }
        //4. 비밀번호 암호화
        updateReqDto.setNewPassword(encoder.encode(updateReqDto.getNewPassword()));

        //5. db에 수정 반영
        user.updateProfile(updateReqDto.getNewPassword());
        usersRepository.save(user);
    }
}
