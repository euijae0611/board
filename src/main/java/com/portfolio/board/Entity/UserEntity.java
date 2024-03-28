package com.portfolio.board.Entity;

import com.portfolio.board.DTO.UserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 100, unique = true)
    private String userEmail;

    @Column
    private String userPW;

    @Column
    private String userPhone;

    @Column
    private String userName;

    @Column
    private String userBirth;

    public static UserEntity toUserEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserEmail(userDTO.getUserEmail());
        userEntity.setUserName(userDTO.getUserName());
        userEntity.setUserPW(userDTO.getUserPW());
        userEntity.setUserPhone(userDTO.getUserPhone());
        userEntity.setUserBirth(userDTO.getUserBirth());
        return userEntity;
    }
}
