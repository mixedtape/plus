package com.example.plus.forum;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ForumListDTO {
    private Long id;
    private String title;
    private String nickname;
    private LocalDateTime createDate;

    public ForumListDTO(Long id, String title, String nickname, LocalDateTime createDate) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.createDate = createDate;
    }
}
