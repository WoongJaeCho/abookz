package kr.basic.abookz.controller;

import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.RatingDTO;
import kr.basic.abookz.dto.ReviewDTO;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.review.ReviewEntity;
import kr.basic.abookz.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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
  private final LikeService likeService;

  @GetMapping("{bookShelfId}/{bookId}")
  private String writeReview(Model model, @PathVariable("bookShelfId") Long bookShelfId,
                             @PathVariable("bookId") Long bookId,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
    if (principalDetails == null) {
      return "redirect:/member/login";
    }
    MemberEntity member = principalDetails.getMember();
    BookShelfDTO shelf = shelfService.findByIdAndBookId(bookShelfId, bookId);
    BookDTO book = bookService.findById(bookId);
    if (reviewService.findByBookShelfId(shelf.getId()) != null) {
      model.addAttribute("review", reviewService.findByBookShelfId(shelf.getId()));
    }
    model.addAttribute("book", book);
    model.addAttribute("shelf", shelf);

    return "/review/review";
  }

  @PostMapping("{bookShelfId}/{bookId}")
  private String saveReview(ReviewDTO reviewDTO, BookShelfDTO bookShelfDTO, @PathVariable("bookShelfId") Long bookShelfId,
                            @PathVariable("bookId") Long bookId,
                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
    if (principalDetails == null) {
      return "redirect:/member/login";
    }
    System.out.println("reviewDTO = " + reviewDTO);
    if (reviewService.findByBookShelfId(bookShelfId) == null) {
      reviewDTO.setBookShelfDTO(shelfService.findById(bookShelfId));
      reviewService.save(reviewDTO);
    } else {
      reviewService.Update(reviewDTO);
    }

    return "redirect:/review/" + bookShelfId + "/" + bookId;
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

  @GetMapping("/reviews")
  public String getReviewsByBookId(
      Model model,
      @RequestParam(defaultValue = "2") Long bookId,
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "5") int pageSize,
      @RequestParam(defaultValue = "최신순") String sort) {

    PageRequest pageRequest = null;


    switch (sort) {
      case "최신순" -> pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "createdDate"));
      case "오래된순" -> pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "createdDate"));
      case "인기순" -> pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "createdDate"));
    }

    Page<ReviewEntity> page = reviewService.getReviewsByBookId(bookId, pageRequest);
    Page<ReviewDTO> dtoPage = page.map(r -> new ReviewDTO(
        r.getId(),
        r.getContent(),
        r.getReviewGrade(),
        r.getCreatedDate(),
        r.getIsSpoilerActive(),
        shelfService.mapEntityToDTO(r.getBookShelf())
    ));

    List<ReviewDTO> reviews = dtoPage.getContent(); // 조회된 데이터
    for (ReviewDTO reviewDTO : reviews) {
      log.info("reviewDTO = {}", reviewDTO); // SLF4J를 사용한 로깅
    }

    int size = reviews.size(); // 조회된 데이터 수
    long totalElements =dtoPage.getTotalElements(); // 전체 데이터 수
    int number = dtoPage.getNumber(); // 페이지 번호
    int totalPages =dtoPage.getTotalPages(); // 전체 페이지 수
    boolean first = dtoPage.isFirst(); // 첫 번째 항목인가?
    boolean next = dtoPage.hasNext(); // 다음 페이지가 있는가?

    model.addAttribute("page", dtoPage);
    model.addAttribute("reviews", reviews);
    model.addAttribute("pageSize", pageSize);


    return "/review/detailReview"; // 뷰 이름 반환
  }


}
