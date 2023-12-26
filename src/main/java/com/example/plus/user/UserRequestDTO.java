package com.example.plus.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 3, message = "닉네임은 최소 3자 이상이어야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,}$", message = "닉네임은 알파벳 대소문자와 숫자로만 구성되어야 합니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String confirmPassword;

    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }

}
