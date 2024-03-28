package com.portfolio.board.Service;


import com.portfolio.board.DTO.UserDTO;
import com.portfolio.board.Entity.UserEntity;
import com.portfolio.board.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(UserDTO userDTO, String phonePrefix, String phoneMiddle, String phoneLast) {
        // 전화번호 조합
        String formattedPhone = String.format("%s-%s-%s", phonePrefix, phoneMiddle, phoneLast);
        userDTO.setUserPhone(formattedPhone);

        // 비밀번호 해시 처리
        String hashedPassword = passwordEncoder.encode(userDTO.getUserPW());
        userDTO.setUserPW(hashedPassword);

        UserEntity userEntity = UserEntity.toUserEntity(userDTO);
        userRepository.save(userEntity);
    }

    public UserDTO login(UserDTO userDTO) {
        Optional<UserEntity> byUserEmail = userRepository.findByUserEmail(userDTO.getUserEmail());
        if(byUserEmail.isPresent()){
            UserEntity userEntity = byUserEmail.get();
            if(passwordEncoder.matches(userDTO.getUserPW(), userEntity.getUserPW())) {
                // 비밀번호 일치
                return UserDTO.toUserDTO(userEntity);
            }
        }
        return null; // 로그인 실패
    }
    public boolean isEmailExists(String email) {
        return userRepository.existsByUserEmail(email);
    }
}
