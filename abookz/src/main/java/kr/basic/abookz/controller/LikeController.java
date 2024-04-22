package kr.basic.abookz.controller;

import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.*;

import kr.basic.abookz.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {
  private final ReviewService reviewService;
  private final MemberService memberService;
  private final BookService bookService;
  private final BookShelfService shelfService;
  private final LikeService likeService;
  private final SimpMessagingTemplate messagingTemplate;


  @PostMapping("/{reviewId}")
  public ResponseEntity<?> toggleLike(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long reviewId) {
    if (principalDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }
    Long memberId = principalDetails.getMember().getId();
    boolean liked = likeService.toggleLike(reviewId, memberId);  // Like 서비스에서 좋아요 상태 토글
    int likesCount = likeService.findAllByReview_Id(reviewId).size();

    ReviewDTO findReview  = reviewService.findById(reviewId);
    findReview.setLiked(liked);
    findReview.setLikesCount(likesCount);


    messagingTemplate.convertAndSend("/topic/likeUpdate", findReview);

    return ResponseEntity.ok(findReview);
  }

}
