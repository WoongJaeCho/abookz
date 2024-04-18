package kr.basic.abookz.controller;

import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.entity.book.TagEnum;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {

    private final BookShelfService bookShelfService;
    private final BookService bookService;

    @GetMapping("/stack")
    public String stackedBooks(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model,
                               RedirectAttributes redirectAttributes){

        Long id = principalDetails.getMember().getId();
        if(id == null) {
            redirectAttributes.addFlashAttribute("fail", "로그인이후 가능합니다");
            return "/member/loginForm";
        }
        List<BookShelfDTO> shelves =bookShelfService.findAllByMemberIdAndTag(id, TagEnum.READ);
        List<BookDTO> books = shelves.stream()
                .map(BookShelfDTO::getBookDTO)
                .flatMap(book -> bookService.findAllByDTOId(book.getId()).stream())
                .toList();
        double booksHeight = 0;
        for(BookShelfDTO shelf : shelves){
            double sizeDepth = (double) shelf.getBookDTO().getSizeDepth();
            booksHeight += sizeDepth;
        }
        System.out.println("shelf = " + shelves);
        model.addAttribute("booksHeight", booksHeight);
        model.addAttribute("books", books);
        model.addAttribute("shelves", shelves);
        return "challenge/stackedBooks";
    }

    @GetMapping("/list")
    public String listedBooks(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model,
                               RedirectAttributes redirectAttributes){

        Long id = principalDetails.getMember().getId();
        if(id == null) {
            redirectAttributes.addFlashAttribute("fail", "로그인이후 가능합니다");
            return "redirect:/member/login";
        }
        List<BookShelfDTO> shelves =bookShelfService.findAllByMemberIdAndTagOrderByIdDesc(id, TagEnum.READ);
        List<BookDTO> books = shelves.stream()
                .map(BookShelfDTO::getBookDTO)
                .flatMap(book -> bookService.findAllByDTOId(book.getId()).stream())
                .toList();

        System.out.println("shelf = " + shelves);
        model.addAttribute("books", books);
        model.addAttribute("shelves", shelves);
        return "challenge/listedBooks";
    }

}
