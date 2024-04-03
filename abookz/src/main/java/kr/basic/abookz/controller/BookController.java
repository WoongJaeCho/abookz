package kr.basic.abookz.controller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.service.AladinService;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import kr.basic.abookz.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final AladinService aladinService;
    private final BookShelfService shelfService;
    private  final BookService bookService;


    @GetMapping("/content/{isbn13}")
    public String getOneDetail(@PathVariable ("isbn13")String isbn13, Model model) throws Exception {
            BookDTO book= aladinService.searchGetOneItem(isbn13);
            model.addAttribute("book", book);
            System.out.println("book = " + book);
        return  "/book/detail";
    }
    //   내 서재로 가기
    @GetMapping("/myshelf")
    public String getMyShelf(HttpSession session, RedirectAttributes redirectAttributes,Model model){
        if(session.getAttribute("id")== null){
            redirectAttributes.addFlashAttribute("fail","로그인부터 해주세요");
            return "redirect:/";
        }
        Long memId = (Long)session.getAttribute("id");
       List<BookShelfEntity> shelf =shelfService.findAllByMemberId(memId);
        List<BookEntity> books = shelf.stream()
                .map(BookShelfEntity::getBook)
                .flatMap(book -> bookService.findAllById(book.getId()).stream())
                .toList();
        model.addAttribute("shelf", shelf);
        model.addAttribute("books",books);
        return "book/myShelf";
    }
}
