package kr.basic.abookz.controller;

import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.RatingDTO;
import kr.basic.abookz.dto.ReviewDTO;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.review.ReviewEntity;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import kr.basic.abookz.service.MemberService;
import kr.basic.abookz.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
  private final ReviewService reviewService;
  private final MemberService memberService;
  private final BookService bookService;
  private final BookShelfService shelfService;
  @GetMapping("{bookShelfId}/{bookId}")
  private String writeReview(Model model, @PathVariable("bookShelfId")Long bookShelfId,
                             @PathVariable("bookId") Long bookId,
                             @AuthenticationPrincipal PrincipalDetails principalDetails ){
    if (principalDetails == null) {
      return "redirect:/member/login";
    }
    MemberEntity member = principalDetails.getMember();
    BookShelfDTO shelf = shelfService.findByIdAndBookId(bookShelfId, bookId);
    BookDTO book = bookService.findById(bookId);
    if (reviewService.findByBookShelfId(shelf.getId())!=null){
      model.addAttribute("review", reviewService.findByBookShelfId(shelf.getId()));
    }
    model.addAttribute("book", book);
    model.addAttribute("shelf", shelf);

    return "/review/review";
  }

  @PostMapping("{bookShelfId}/{bookId}")
  private String saveReview(ReviewDTO reviewDTO, BookShelfDTO bookShelfDTO, @PathVariable("bookShelfId")Long bookShelfId,
                             @PathVariable("bookId") Long bookId,
                             @AuthenticationPrincipal PrincipalDetails principalDetails ){
    if (principalDetails == null) {
      return "redirect:/member/login";
    }
    System.out.println("reviewDTO = " + reviewDTO);
    if (reviewService.findByBookShelfId(bookShelfId)==null) {
      reviewDTO.setBookShelfDTO(shelfService.findById(bookShelfId));
      reviewService.save(reviewDTO);
    }else{
      reviewService.Update(reviewDTO);
    }

    return "redirect:/review/"+bookShelfId+"/"+bookId;
  }

  @PostMapping("/rating")
  public ResponseEntity<String> submitRating(@RequestBody RatingDTO ratingDTO,
                                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
    if (principalDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }
    System.out.println("ratingDTO = " + ratingDTO);
    BookShelfDTO shelf = shelfService.findByIdAndBookId(ratingDTO.getBookShelfId(), ratingDTO.getBookId());
    shelf.setBookShelfGrade(ratingDTO.getRating());
    shelfService.updateGrade(shelf);
    System.out.println("shelf = " + shelf);
    return ResponseEntity.ok().body("{\"message\":\"별점이 성공적으로 등록되었습니다.\"}");
  }

//  @GetMapping("/reviews")
//  public Page<ReviewEntity> getReviewsByBookId(@RequestParam Long bookId) {
//    PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC,"name"));
//    Page<ReviewEntity> reviewsByBookId = reviewService.getReviewsByBookId(bookId, pageRequest);
//
//  }
}
