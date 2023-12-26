package com.example.plus.forum;

import com.example.plus.comment.Comment;
import com.example.plus.comment.CommentDTO;
import com.example.plus.user.User;
import com.example.plus.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForumService {

    private final ForumRepository forumRepository;
    private final UserRepository userRepository;


    public void createForum(ForumRequestDTO forumRequestDTO, String username) {
        User user = userRepository.findByNickname(username)
                .orElseThrow(
                        () -> new IllegalArgumentException("사용자를 찾을 수 없습니다. 사용자명: " + username));

        Forum forum = new Forum();
        forum.setTitle(forumRequestDTO.getTitle());
        forum.setContent(forumRequestDTO.getContent());
        forum.setCreateDate(LocalDateTime.now());
        forum.setUser(user);

        forumRepository.save(forum);
    }


    public List<ForumListDTO> getAllForumsOrderedByCreateDate() {
        List<Forum> forums = forumRepository.findByOrderByCreateDateDesc();

        return forums.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ForumDetailsDTO getForumDetailsById(Long id) {
        Optional<Forum> forumOptional = forumRepository.findById(id);

        if (forumOptional.isPresent()) {
            Forum forum = forumOptional.get();
            return convertToDetailsDTO(forum);
        } else {
            throw new IllegalArgumentException("게시물을 찾을 수 없습니다.");
        }
    }

    public void updateForum(Long id, ForumRequestDTO forumRequestDTO, String username) {
        Optional<Forum> forumOptional = forumRepository.findById(id);

        if (forumOptional.isPresent()) {
            Forum forum = forumOptional.get();

            if (!forum.getUser().getNickname().equals(username)) {
                throw new IllegalArgumentException("게시글 작성자만 수정할 수 있습니다.");
            }

            forum.setTitle(forumRequestDTO.getTitle());
            forum.setContent(forumRequestDTO.getContent());

            forum.setCreateDate(LocalDateTime.now());

            forumRepository.save(forum);
        } else {
            throw new IllegalArgumentException("게시물을 찾을 수 없습니다.");
        }
    }


    public void deleteForum(Long id, String username) {
        Optional<Forum> forumOptional = forumRepository.findById(id);

        if (forumOptional.isPresent()) {
            Forum forum = forumOptional.get();

            if (!forum.getUser().getNickname().equals(username)) {
                throw new IllegalArgumentException("게시글 작성자만 삭제할 수 있습니다.");
            }

            forumRepository.delete(forum);
        } else {
            throw new IllegalArgumentException("게시물을 찾을 수 없습니다.");
        }
    }

    private ForumListDTO convertToDTO(Forum forum) {
        return new ForumListDTO(
                forum.getId(),
                forum.getTitle(),
                forum.getUser().getNickname(),
                forum.getCreateDate()
        );
    }

    private ForumDetailsDTO convertToDetailsDTO(Forum forum) {

        List<CommentDTO> commentDTOs = convertCommentToDTO(forum.getComments());

        return new ForumDetailsDTO(
                forum.getId(),
                forum.getTitle(),
                forum.getUser().getNickname(),
                forum.getCreateDate(),
                forum.getContent(),
                commentDTOs
        );
    }


}