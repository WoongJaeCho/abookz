package kr.basic.abookz.controller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

import static kr.basic.abookz.entity.book.TagEnum.CURRENTLY_READING;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BookShelfService bookShelfService;
    private final BookService bookService;
    @GetMapping("/")
    public String index(Model model, HttpSession session) {

        if(session.getAttribute("id") != null) {
            Long memId = (Long) session.getAttribute("id");
            List<BookShelfEntity> shelves = bookShelfService.findAllByMemberIdAndTag(memId, CURRENTLY_READING);
            List<BookEntity> books = shelves.stream()
                    .map(BookShelfEntity::getBook)
                    .flatMap(book -> bookService.findAllById(book.getId()).stream())
                    .toList();
            System.out.println("books = " + books);
            LocalDate currentDate = LocalDate.now();
            System.out.println(currentDate);
            System.out.println(shelves.get(0).getStartDate());
            if( shelves.size() != 0 || books.size() != 0 ) {
                model.addAttribute("currentDate", currentDate);
                model.addAttribute("books", books);
                model.addAttribute("shelves", shelves);
            }

        }

        return "index";
    }

}
