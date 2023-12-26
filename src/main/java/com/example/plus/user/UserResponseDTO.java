package com.example.plus.user;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String nickname;


    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
    }
}
