package CY.cymake.Security;

import CY.cymake.Domain.Auth.Dto.CustomUserInfoDto;
import CY.cymake.Entity.UsersEntity;
import CY.cymake.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        //id로 user 찾음
        UsersEntity user = usersRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));
        CustomUserInfoDto dto = mapper.map(user, CustomUserInfoDto.class);

        return new CustomUserDetails(dto);
    }
}
