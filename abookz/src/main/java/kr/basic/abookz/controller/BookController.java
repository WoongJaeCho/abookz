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
import org.springframework.data.domain.Pageable;
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

  @GetMapping("/content/{isbn13}")
  public String getOneDetail(@PathVariable("isbn13") String isbn13, Model model,
                             @RequestParam(defaultValue = "0") int pageNumber,
                             @RequestParam(defaultValue = "5") int pageSize,
                             @RequestParam(defaultValue = "최신순") String sort,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
    BookDTO book = aladinService.searchGetOneItem(isbn13);
    MemberEntity member;
    if (principalDetails!=null) {
      member = principalDetails.getMember();
      try {
        BookShelfDTO findShelf = shelfService.findByMember_IdAndBook_ISBN13(member.getId(), book.getISBN13());
        model.addAttribute("shelf",findShelf);
        book.setId(findShelf.getId());
      }catch (RuntimeException e){
        log.error("책꽂이나 ISBN에 해당하는 데이터가 없습니다." );
      }

    }
//    book.setId(bookService.findByDTOISBN13(Long.valueOf(isbn13)).getId());
    model.addAttribute("book", book);


    System.out.println("book = " + book);

    return "book/detail";
  }

  @GetMapping("/category/{category}")
  public String choiceCategory(@PathVariable("category") String category, @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size, Model model) throws Exception {
    Pageable pageable = PageRequest.of(page, size);
    Page<BookDTO> bookPage = aladinService.choiceGetCategoryList(category, pageable);
    System.out.println("bookPage = " + bookPage);
    model.addAttribute("categoryCheck", category);
    model.addAttribute("books", bookPage.getContent());
    model.addAttribute("currentPage", bookPage.getNumber());
    model.addAttribute("totalPages", bookPage.getTotalPages());
    model.addAttribute("totalItems", bookPage.getTotalElements());
    return "book/category";
  }

  /* 베스트 셀러나 신간 리스트 가져오기 */
  @GetMapping("/bestSeller/{type}")
  public String choiceQueryType(@PathVariable("type") String type,
                                @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model)
      throws Exception {
    Pageable pageable = PageRequest.of(page, size);
    Page<BookDTO> bookPage = aladinService.getQueryPagingList(type, pageable);
    model.addAttribute("books", bookPage.getContent());
    model.addAttribute("currentPage", bookPage.getNumber());
    model.addAttribute("totalPages", bookPage.getTotalPages());
    model.addAttribute("totalItems", bookPage.getTotalElements());
    return "book/category";
  }
}
