package backend.keumbang.auth.service;

import backend.keumbang.auth.entity.User;
import backend.keumbang.auth.repository.UserRepository;
import backend.keumbang.auth.security.CustomUserDetails;
import backend.keumbang.common.constants.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND));

        return new CustomUserDetails(user);
    }
}