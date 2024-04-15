package kr.basic.abookz.controller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.config.auth.PrincipalDetailsService;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.ReviewDTO;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.review.ReviewEntity;
import kr.basic.abookz.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BookController {

  private final AladinService aladinService;
  private final BookShelfService shelfService;
  private final BookService bookService;
  private final ReviewService reviewService;
  private final PrincipalDetailsService principalDetailsService;

  @GetMapping("/search")
  public String getSearh() {
    return "/index";
  }

  @GetMapping("/content/{isbn13}")
  public String getOneDetail(@PathVariable("isbn13") String isbn13, Model model,
                             @RequestParam(defaultValue = "0") int pageNumber,
                             @RequestParam(defaultValue = "5") int pageSize,
                             @RequestParam(defaultValue = "최신순") String sort) throws Exception {
    BookDTO book = aladinService.searchGetOneItem(isbn13);
    model.addAttribute("book", book);
    System.out.println("book = " + book);

    PageRequest pageRequest = null;


    switch (sort) {
      case "최신순" -> pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
      case "오래된순" -> pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "createdDate"));
      case "인기순" ->
          pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "reviewGrade"));  // 가정
    }

    if (bookService.findByDTOISBN13(Long.valueOf(isbn13)) != null) {
      BookDTO findBook = bookService.findByDTOISBN13(Long.valueOf(isbn13));
      Long bookId = findBook.getId();

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
      long totalElements = dtoPage.getTotalElements(); // 전체 데이터 수
      int number = dtoPage.getNumber(); // 페이지 번호
      int totalPages = dtoPage.getTotalPages(); // 전체 페이지 수
      boolean first = dtoPage.isFirst(); // 첫 번째 항목인가?
      boolean next = dtoPage.hasNext(); // 다음 페이지가 있는가?

      model.addAttribute("page", dtoPage);
      model.addAttribute("reviews", reviews);
      model.addAttribute("pageSize", pageSize);
    }

    return "/book/detail";
  }

  @GetMapping("/category/{category}")
  public String choiceCategory(@PathVariable("category") String category, Model model) throws Exception {
    List<BookDTO> getCategoryList = aladinService.choiceGetCategoryList(category);
    model.addAttribute("books", getCategoryList);
    System.out.println("getCategoryList = " + getCategoryList);
    return "book/category";
  }

  /* 베스트 셀러나 신간 리스트 가져오기*/
  @GetMapping("/bestSeller/{type}")
  public String choiceQueryType(@PathVariable("type") String type, Model model) throws Exception {
    List<BookDTO> queryTypeList = aladinService.getQueryTypeList(type);
    model.addAttribute("books", queryTypeList);
    System.out.println("getQueryTypeList = " + queryTypeList);
    return "book/queryType";
  }
}
