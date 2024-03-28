package com.portfolio.board.Service;


import com.portfolio.board.DTO.UserDTO;
import com.portfolio.board.Entity.UserEntity;
import com.portfolio.board.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(UserDTO userDTO, String phonePrefix, String phoneMiddle, String phoneLast) {
        // 전화번호 조합
        String formattedPhone = String.format("%s-%s-%s", phonePrefix, phoneMiddle, phoneLast);
        userDTO.setUserPhone(formattedPhone);

        UserEntity userEntity = UserEntity.toUserEntity(userDTO);
        userRepository.save(userEntity);
    }

    public UserDTO login(UserDTO userDTO) {
        Optional<UserEntity> byUserEmail = userRepository.findByUserEmail(userDTO.getUserEmail());
        if(byUserEmail.isPresent()){
            //해당 이메일을 가진 회원 정보가 있음.
            UserEntity userEntity = byUserEmail.get();
            if(userEntity.getUserPW().equals(userDTO.getUserPW())) {
                //이메일 있고, 비밀번호 일치
                UserDTO dto = UserDTO.toUserDTO(userEntity);
                return dto;
            } else {
                //이메일 있지만, 비밀번호 불일치
                return null;
            }
        } else {
            //해당 이메일을 가진 회원 정보가 없음.
            return null;
        }
    }
    public boolean isEmailExists(String email) {
        return userRepository.existsByUserEmail(email);
    }
}
