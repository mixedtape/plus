package com.example.plus.forum;


import com.example.plus.comment.CommentDTO;
import java.time.LocalDateTime;
import java.util.List;

public class ForumDetailsDTO {

    private Long id;
    private String title;
    private String nickname; // 작성자명
    private LocalDateTime createDate;
    private String content;
    private List<CommentDTO> comments;

    public ForumDetailsDTO(Long id, String title, String nickname, LocalDateTime createDate, String content, List<CommentDTO> comments) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.createDate = createDate;
        this.content = content;
        this.comments = comments;
    }
}