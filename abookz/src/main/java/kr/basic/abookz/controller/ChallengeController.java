package kr.basic.abookz.controller;

import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
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

import static kr.basic.abookz.entity.book.TagEnum.*;

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
            return "member/loginForm";
        }
        List<BookShelfDTO> shelves =bookShelfService.findAllByMemberIdAndTag(id, READ);
        List<BookDTO> books = shelves.stream()
                .map(BookShelfDTO::getBookDTO)
                .flatMap(book -> bookService.findAllByDTOId(book.getId()).stream())
                .toList();
        double booksHeight = 0;
        for(BookShelfDTO shelf : shelves){
            double sizeDepth = (double) shelf.getBookDTO().getSizeDepth();
            booksHeight += sizeDepth;
        }
        double averageHeightOfReadBooks = bookShelfService.averageHeightOfReadBooks();
        double averageBooksOfRead = bookShelfService.averageBooksOfRead();
        System.out.println("averageBooksOfRead = " + averageBooksOfRead);

        model.addAttribute("averageHeight", averageHeightOfReadBooks);
        model.addAttribute("averageBooks", averageBooksOfRead);
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
        List<BookShelfDTO> shelves =bookShelfService.findAllByMemberIdAndTagOrderByIdDesc(id, READ);
        List<BookDTO> books = shelves.stream()
                .map(BookShelfDTO::getBookDTO)
                .flatMap(book -> bookService.findAllByDTOId(book.getId()).stream())
                .toList();

        System.out.println("shelf = " + shelves);
        model.addAttribute("books", books);
        model.addAttribute("shelves", shelves);
        return "challenge/listedBooks";
    }

    @GetMapping("/scale")
    public String scale(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model,
                              RedirectAttributes redirectAttributes){

        Long id = principalDetails.getMember().getId();
        double currentMemberSum = bookShelfService.findAllByMemberIdAndTag(id, READ)
            .stream()
            .mapToDouble(shelf -> shelf.getBookDTO().getWeight()/1000.0)
            .sum();

//        double AverageSum = bookShelfService.findAllByTag(READ)
//            .stream()
//            .mapToDouble(shelf -> shelf.getBookDTO().getWeight()/1000.0)
//            .sum();

        double averageWeightOfReadBooks = bookShelfService.averageWeightOfReadBooks()/1000.0;

        System.out.println("currentMemberSum = " + currentMemberSum);
        System.out.println("averageWeightOfReadBooks = " + averageWeightOfReadBooks);
        model.addAttribute("memberSum", currentMemberSum);
        model.addAttribute("sumAverage", averageWeightOfReadBooks);

        return "challenge/balanceScale";
    }

}
