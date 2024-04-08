package kr.basic.abookz.controller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.admin.SlideCardDTO;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import kr.basic.abookz.service.admin.SlideCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;
import java.time.LocalDate;
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
    public String index(Model model, HttpSession session) {

        List<SlideCardDTO> slideCard = adminService.findTop3();
        System.out.println("slideCard = " + slideCard);

        if( slideCard.size() != 0) {
            model.addAttribute("slideCard", slideCard);
        }

            Long memId = (Long) session.getAttribute("id");
            List<BookShelfDTO> shelves = bookShelfService.findAllByMemberIdAndTag(memId, CURRENTLY_READING);
            List<BookDTO> books = shelves.stream()
                    .map(BookShelfDTO::getBookDTO)
                    .flatMap(book -> bookService.findAllByDTOId(book.getId()).stream())
                    .toList();
            LocalDate currentDate = LocalDate.now();

            for(BookShelfDTO shelf : shelves){
                Duration duration = Duration.between(shelf.getStartDate(), LocalDateTime.now());
                shelf.setDays(duration.toDays());
            }

            if( shelves.size() != 0 || books.size() != 0 ) {
                model.addAttribute("currentDate", currentDate);
                model.addAttribute("books", books);
                model.addAttribute("shelves", shelves);
            }

        return "index";
    }

}
