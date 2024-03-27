package com.portfolio.board.DTO;

import com.portfolio.board.Entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO {
    private long id;
    private String userName;
    private String userPhone;
    private String userPW;
    private String userEmail;
    private String userBirth;

    public static UserDTO toUserDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserEmail(userEntity.getUserEmail());
        userDTO.setUserName(userEntity.getUserName());
        userDTO.setUserPW(userEntity.getUserPW());
        userDTO.setUserPhone(userEntity.getUserPhone());
        userDTO.setUserBirth(userEntity.getUserBirth());
        return userDTO;
    }
}
