package com.example.plus.comment;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String content;
    private String nickname; // 작성자명
    private LocalDateTime createDate;


    public CommentDTO(Long id, String content, String nickname, LocalDateTime createDate) {
        this.id = id;
        this.content = content;
        this.nickname = nickname;
        this.createDate = createDate;
    }
}
