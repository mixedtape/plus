package com.example.plus.forum;

import com.example.plus.CommonResponseDTO;
import com.example.plus.Jwt.JwtUtil;
import com.example.plus.user.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forums")
public class ForumController {

    private final ForumService forumService;
    private final JwtUtil jwtUtil;

    public ForumController(ForumService forumService, JwtUtil jwtUtil) {
        this.forumService = forumService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> createForum(
            @RequestBody ForumRequestDTO forumRequestDTO,
            HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        //Http요청에서 토큰을 추출

        if (jwtUtil.validateToken(token)) {
            //토큰의 유효성을 검증
            String username = jwtUtil.getUserInfoFromToken(token).getSubject();
            // 토큰에서 사용자 정보를 추출
            forumService.createForum(forumRequestDTO, username);
            return ResponseEntity.ok()
                    .body(new CommonResponseDTO("게시글이 성공적으로 생성되었습니다.", HttpStatus.OK.value()));
        } else {
            return ResponseEntity.badRequest()
                    .body(new CommonResponseDTO("인증되지 않았습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponseDTO> updateForum(
            @PathVariable Long id,
            @RequestBody ForumRequestDTO forumRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletRequest request
    ) {
        String token = jwtUtil.resolveToken(request);

        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUserInfoFromToken(token).getSubject();

            try {
                forumService.updateForum(id, forumRequestDTO, username);
                return ResponseEntity.ok()
                        .body(new CommonResponseDTO("게시글이 성공적으로 수정되었습니다.", HttpStatus.OK.value()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(new CommonResponseDTO(e.getMessage(),
                        HttpStatus.BAD_REQUEST.value()));
            }
        } else {
            return ResponseEntity.badRequest()
                    .body(new CommonResponseDTO("인증되지 않았습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDTO> deleteForum(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletRequest request
    ) {
        String token = jwtUtil.resolveToken(request);

        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUserInfoFromToken(token).getSubject();

            try {
                forumService.deleteForum(id, username);
                return ResponseEntity.ok().body(new CommonResponseDTO("게시글이 성공적으로 삭제되었습니다.", HttpStatus.OK.value()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(new CommonResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
            }
        } else {
            return ResponseEntity.badRequest().body(new CommonResponseDTO("인증되지 않았습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ForumListDTO>> getAllForums() {
        List<ForumListDTO> forums = forumService.getAllForumsOrderedByCreateDate();
        return ResponseEntity.ok(forums);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getForumDetails(@PathVariable Long id) {
        try {
            ForumDetailsDTO forumDetails = forumService.getForumDetailsById(id);
            return ResponseEntity.ok(forumDetails);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CommonResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}
