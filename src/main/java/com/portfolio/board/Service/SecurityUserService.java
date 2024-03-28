package com.portfolio.board.Service;

import com.portfolio.board.Entity.UserEntity;
import com.portfolio.board.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SecurityUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public SecurityUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // 이 예시에서는 모든 사용자를 "USER" 권한을 가지고 있다고 가정합니다.
        // 실제 어플리케이션에서는 userEntity에서 권한을 가져와서 설정해야 할 수도 있습니다.
        return new User(userEntity.getUserEmail(), userEntity.getUserPW(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
