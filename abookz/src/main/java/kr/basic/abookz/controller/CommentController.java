package kr.basic.abookz.controller;

import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.*;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.review.CommentEntity;
import kr.basic.abookz.entity.review.ReviewEntity;
import kr.basic.abookz.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kr.basic.abookz.entity.member.RoleEnum.ROLE_ADMIN;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
  private final CommentService commentService;
  private final MemberService memberService;

  @GetMapping("/{reviewId}")
  public ResponseEntity<Map<String, Object>> getComments(@PathVariable("reviewId") Long reviewId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
    Slice<CommentDTO> comments = commentService.findAllByReview_Id(reviewId, pageable);

    Map<String, Object> response = new HashMap<>();
    response.put("comments", comments.getContent());
    response.put("hasNext", comments.hasNext());

    return ResponseEntity.ok(response);
  }

  @PostMapping("/{reviewId}")
  public ResponseEntity<?> submitComment(@PathVariable("reviewId") Long reviewId,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails,
                                         @RequestBody CommentDTO commentDTO) {
    if (principalDetails == null) {
      Map<String, Object> response = new HashMap<>();
      response.put("message", "로그인이 필요합니다.");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // 로그인 필요 상태 코드 변경
    }

    try {
      // 사용자 정보와 리뷰 ID 설정
      commentDTO.setMember(memberService.findById(principalDetails.getMember().getId()));
      commentDTO.setCreatedDate(LocalDateTime.now());
      commentDTO.setReviewId(reviewId);

      // 댓글 저장
      CommentDTO savedComment = commentService.save(commentDTO);

      // 성공 응답과 함께 저장된 CommentDTO 반환
      return ResponseEntity.ok(Map.of("success", true, "message", "댓글이 등록됐습니다.", "comment", savedComment));
    } catch (DataAccessException e) {
      log.error("Database error: {}", e.getMessage());
      return new ResponseEntity<>(Map.of("success", false, "message", "데이터베이스 에러: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
      log.error("Error saving comment: {}", e.getMessage());
      return new ResponseEntity<>(Map.of("success", false, "message", "댓글 등록 에러: " + e.getMessage()), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteComment(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
    try {
      // 현재 인증된 사용자의 권한을 확인합니다.
      boolean isAdmin = principalDetails.getMember().getRole().equals(ROLE_ADMIN);

      // ADMIN 역할이면 바로 삭제합니다.
      if (isAdmin) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
      }

      // 일반 사용자의 경우 소유자인지 확인합니다.
      Long currentUserId = principalDetails.getMember().getId();
      System.out.println("currentUserId = " + currentUserId);
      Long ownerId = commentService.getOwnerIdById(id).getId();
      System.out.println("ownerId = " + ownerId);

      if (currentUserId != ownerId) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인의 댓글만 삭제할 수 있습니다.");
      }

      commentService.deleteComment(id);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 에러");
    }
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<?> updateComment(@PathVariable("id") Long id,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails,
                                         @RequestBody String text) {
    try {
      // 댓글 소유자 확인
      Long ownerId = commentService.getOwnerIdById(id).getId();
      Long currentUserId = principalDetails.getMember().getId();

      // ADMIN 또는 댓글 작성자만 수정 가능
      boolean isAdmin = principalDetails.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
      if (!isAdmin && !currentUserId.equals(ownerId)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인의 댓글만 수정할 수 있습니다.");
      }

      // 댓글 업데이트 로직
      CommentDTO comment = commentService.findById(id);
      commentService.update(comment,text);
      comment.setComment(text);

      return ResponseEntity.ok(Map.of("success", true, "message", "댓글이 수정되었습니다.","comment",comment));
    } catch (Exception e) {
      log.error("Error updating comment: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 수정 에러");
    }
  }

}