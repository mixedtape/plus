package com.example.plus.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter

public class User  {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "닉네임을 입력해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,50}$", message = "닉네임은 알파벳과 숫자로 3자이상 입력해주세요")
    @Column(length = 50)
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    @Column(length = 100)
    private String password;

    // 비밀번호 확인 필드
    @NotBlank(message = "비밀번호 확인은 필수입니다")
    private String confirmPassword;

    // 생성자, 게터, 세터 등 필요한 메서드들...

    // 비밀번호 확인 로직
    public boolean confirmPassword() {
        return password != null && password.equals(confirmPassword);
    }
}
