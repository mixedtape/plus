package com.example.plus.user;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public UserResponseDTO getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(UserResponseDTO::new).orElse(null);
    }

    public void createUser(UserRequestDTO userRequestDTO) {

        if (userRepository.existsByNickname(userRequestDTO.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }


        if (!userRequestDTO.isPasswordMatching()) {
            throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        }


        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

        try {
            User savedUser = userRepository.save(convertToEntity(userRequestDTO));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }
    }

    public UserResponseDTO updateUser(Long userId, UserRequestDTO userRequestDTO) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다.");
        }

        User existingUser = userOptional.get();

        // 닉네임 중복 체크
        if (!existingUser.getNickname().equals(userRequestDTO.getNickname())
                && userRepository.existsByNickname(userRequestDTO.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        if (userRequestDTO.getPassword() != null) {
            if (!userRequestDTO.isPasswordMatching()) {
                throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
            }
            existingUser.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        existingUser.setNickname(userRequestDTO.getNickname());
        User updatedUser = userRepository.save(existingUser);

        return new UserResponseDTO(updatedUser);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }






    private UserResponseDTO convertToDTO(User user) {
        return new UserResponseDTO(user);
    }

    private User convertToEntity(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setNickname(userRequestDTO.getNickname());
        user.setPassword(userRequestDTO.getPassword());
        return user;
    }
}