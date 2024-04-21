package kr.basic.abookz.controller;

import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.*;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.review.CommentEntity;
import kr.basic.abookz.entity.review.ReviewEntity;
import kr.basic.abookz.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
  private final ReviewService reviewService;
  private final MemberService memberService;
  private final BookService bookService;
  private final BookShelfService shelfService;
  private final LikeService likeService;
  private final CommentService commentService;

//  @GetMapping("/{reviewId}")
//  public String comment(@PathVariable("reviewId") Long reviewId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
//
//    return "review/comment";
//  }

  @GetMapping("/{reviewId}")
  public ResponseEntity<Map<String, Object>> comment(@PathVariable("reviewId") Long reviewId, Model model,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails,
                                                     @RequestParam(defaultValue = "") String query,
                                                     @RequestParam(defaultValue = "0") int pageNumber,
                                                     @RequestParam(defaultValue = "3") int pageSize) {

    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
    Slice<CommentDTO> comments = commentService.findAllByReview_Id(reviewId, pageRequest);

//    int getNumber(); // 현재 페이지
//    int getSize(); // 페이지 크기
//    int getNumberOfElements(); // 현재 페이지에 나올 데이터 수
//    List<T> getContent(); // 조회된 데이터
//    boolean hasContent(); // 조회된 데이터 존재 여부
//    Sort getSort(); // 정렬 정보
//    boolean isFirst(); // 현재 페이지가 첫 페이지 인지 여부
//    boolean isLast(); // 현재 페이지가 마지막 페이지 인지 여부
//    boolean hasNext(); // 다음 페이지 여부
//    boolean hasPrevious(); // 이전 페이지 여부
//    Pageable getPageable(); // 페이지 요청 정보
//    Pageable nextPageable(); // 다음 페이지 객체
//    Pageable previousPageable(); //이전 페이지 객체
//	<U> Slice<U> map(Function<? super T, ? extends U> converter); //변환기

    Map<String, Object> response = new HashMap<>();
    response.put("comments", comments);

    return ResponseEntity.ok(response);
  }



  @PostMapping("/{reviewId}")
  public ResponseEntity<String> submitComment(@PathVariable("reviewId") Long reviewId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails,
                              @RequestBody CommentDTO commentDTO) {

    try {
      CommentDTO comment = new CommentDTO();
      comment.setComment(commentDTO.getComment());
      comment.setReview(reviewService.findById(commentDTO.getReview().getId()));
      comment.setMember(memberService.findById(principalDetails.getMember().getId()));
      comment.setCreatedDate(LocalDateTime.now());
      commentService.save(comment);
      return new ResponseEntity<>("Comment added successfully", HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>("Error saving comment: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

}
