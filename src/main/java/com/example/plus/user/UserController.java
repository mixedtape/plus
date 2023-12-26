package com.example.plus.user;

import com.example.plus.CommonResponseDTO;
import com.example.plus.Jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    private UserLoginService userLoginService;
    private JwtUtil jwtUtil;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        UserResponseDTO userDTO = userService.getUserById(userId);
        return userDTO != null
                ? ResponseEntity.ok(userDTO)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
       try { userService.createUser(userRequestDTO);
           return ResponseEntity.status(HttpStatus.CREATED.value()).body(new CommonResponseDTO("회원가입 성공", HttpStatus.CREATED.value()));

       }catch (IllegalArgumentException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body(new CommonResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
       }
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDTO> login(@RequestBody UserRequestDTO userRequestDto, HttpServletResponse response) {
        try {
            userLoginService.loginUser(userRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        String jwtToken = jwtUtil.createToken(userRequestDto.getNickname());
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtToken);

        return ResponseEntity.ok().build();
    }





    @PutMapping("/{userId}")
    public ResponseEntity<CommonResponseDTO> updateUser(@PathVariable Long userId, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        try {
           userService.updateUser(userId, userRequestDTO);
            return ResponseEntity.ok(new CommonResponseDTO("업데이트 성공", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
