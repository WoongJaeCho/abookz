package kr.basic.abookz.controller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.admin.SlideCardDTO;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import kr.basic.abookz.service.admin.SlideCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static kr.basic.abookz.entity.book.TagEnum.CURRENTLY_READING;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SlideCardService adminService;
    private final BookShelfService bookShelfService;
    private final BookService bookService;
    @GetMapping("/")
    public String index(Model model, HttpSession session, @AuthenticationPrincipal PrincipalDetails principalDetails) {


        List<SlideCardDTO> slideCard = adminService.findAllOrderByIdx();


        if( slideCard.size() != 0) {
            model.addAttribute("slideCard", slideCard);
        }
        if(principalDetails != null) {
            Long memId = principalDetails.getMember().getId();

            List<BookShelfDTO> shelves = bookShelfService.findAllByMemberIdAndTag(memId, CURRENTLY_READING);
            List<BookDTO> books = shelves.stream()
                    .map(BookShelfDTO::getBookDTO)
                    .flatMap(book -> bookService.findAllByDTOId(book.getId()).stream())
                    .toList();

            System.out.println("books = " + books);

            for (BookShelfDTO shelf : shelves) {
                Duration duration = Duration.between(shelf.getStartDate(), LocalDateTime.now());
                shelf.setDays(duration.toDays());
            }

            if (shelves.size() != 0 && books.size() != 0) {
                model.addAttribute("books", books);
                model.addAttribute("shelves", shelves);
            }
        }

        return "index";
    }

}
