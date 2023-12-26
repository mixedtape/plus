package com.example.plus.user;

import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NoArgsConstructor
public class UserLoginService {

    private  UserRepository userRepository;
    private  PasswordEncoder passwordEncoder;



    // 기존 메서드들은 여기에 있을 것이라 가정하고, 로그인을 처리하는 메서드 추가
    public UserResponseDTO loginUser(UserRequestDTO userRequestDTO) {
        String nickname = userRequestDTO.getNickname();
        String password = userRequestDTO.getPassword();

        // 닉네임으로 사용자 찾기
        Optional<User> userOptional = userRepository.findByNickname(nickname);

        if (userOptional.isPresent()) {
            // 사용자가 존재하면 비밀번호 일치 여부 확인
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                // 비밀번호 일치하면 로그인 성공
                return new UserResponseDTO(user);
            }
        }


        // 사용자가 존재하지 않거나 비밀번호 불일치 시 예외 발생
        throw new IllegalArgumentException("닉네임 또는 패스워드를 확인해주세요.");
    }
}
