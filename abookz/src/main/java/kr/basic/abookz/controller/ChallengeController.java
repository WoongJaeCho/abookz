package kr.basic.abookz.controller;

import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import kr.basic.abookz.service.MemberService;
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
    private final MemberService memberService;

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
    public String scale(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        // 현재 사용자의 ID를 가져옵니다.
        Long id = principalDetails.getMember().getId();

        // 현재 사용자의 정보를 조회합니다.
        MemberDTO findMember = memberService.findById(id);

        // 현재 사용자가 읽은 책들의 총 무게를 계산합니다. (단위: 킬로그램)
        double currentMemberSum = bookShelfService.findAllByMemberIdAndTag(id,READ)
            .stream()
            .mapToDouble(shelf -> shelf.getBookDTO().getWeight() / 1000.0)
            .sum();

        // 모든 사용자의 읽은 책들의 평균 무게를 계산합니다. (현재 사용자 제외, 단위: 킬로그램)
        double averageWeightOfReadBooks = bookShelfService.averageWeightOfReadBooksExcludingCurrent(id) / 1000.0;

        // 결과 로그 출력
        System.out.println("currentMemberSum = " + currentMemberSum);
        System.out.println("averageWeightOfReadBooks = " + averageWeightOfReadBooks);

        // 모델에 데이터를 추가하여 뷰에 전달합니다.
        model.addAttribute("memberSum", currentMemberSum);
        model.addAttribute("member", findMember);
        model.addAttribute("sumAverage", averageWeightOfReadBooks);

        return "challenge/balanceScale"; // 결과를 표시할 뷰 페이지
    }

}
