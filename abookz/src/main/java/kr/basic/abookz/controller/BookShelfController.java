package kr.basic.abookz.controller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.entity.book.TagEnum;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookShelfController {

    private final BookShelfService shelfService;
    private final BookService bookService;
    //   내 서재로 가기
    @GetMapping("/myshelf")
    public String getMyShelf(HttpSession session, RedirectAttributes redirectAttributes, Model model
            , @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long id = principalDetails.getMember().getId();
        if(id == null) {
            redirectAttributes.addFlashAttribute("fail", "로그인이후 가능합니다");
            return "redirect:/member/login";
        }
        List<BookShelfDTO> shelf =shelfService.findAllDTOByMemberId(id);
       //밑에는 각 사이즈 가져오기 내서재들 옆 숫자표시 몇권있는지
        int read = (int) shelf.stream()
                .map(item -> item.getTag())
                .filter(tag -> tag != null && tag.getKorean().equals("읽은책"))
                .count();
        int want = (int) shelf.stream()
                .map(BookShelfDTO::getTag)
                .filter(tag -> tag == null || tag == TagEnum.READ)
                .count();
        int current=(int)shelf.stream().map(item -> item.getTag())
                .filter(tag -> tag != null && tag.getKorean().equals("읽고있는책")).count();

        List<BookDTO> books = shelf.stream()
                .map(BookShelfDTO::getBookDTO)
                .flatMap(book -> bookService.findAllByDTOId(book.getId()).stream())
                .toList();
        //숫자 카운터 보내기용
        model.addAttribute("read", read);
        model.addAttribute("want", want);
        model.addAttribute("current",current);
        
        model.addAttribute("shelf", shelf);
        model.addAttribute("books",books);
        return "book/myShelf";
    }
    @GetMapping("/myshelf/tag/{tag}")
    public String myShelfTag(@PathVariable ("tag")String  tag, Model model,   @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long memId= principalDetails.getMember().getId();
        System.out.println("tag.toUpperCase() = " + tag.toUpperCase());
        if(memId== null){
            return "member/login";
        }
        List<BookShelfDTO> myShelf;
        if (tag.equalsIgnoreCase("ALL")) {
            myShelf = shelfService.findAllDTOByMemberId(memId);
        } else {
            try {
                TagEnum tagEnum = TagEnum.valueOf(tag.toUpperCase());
                myShelf = shelfService.findAllByMemberIdAndTag(memId,tagEnum );
            } catch (IllegalArgumentException e) {
                // 태그가 유효하지 않은 경우의 처리
                return "errorPage"; // 적절한 에러 페이지로 리다이렉트
            }
        }

        int read = (int) myShelf.stream()
                .map(BookShelfDTO::getTag)
                .filter(tagEnum -> tagEnum != null && tagEnum.getKorean().equals("읽은책"))
                .count();
        int want = (int) myShelf.stream()
                .map(BookShelfDTO::getTag)
                .filter(tagEnum -> tagEnum == null || tagEnum.getKorean().equals("읽고싶은책"))
                .count();
        int current=(int)myShelf.stream().map(BookShelfDTO::getTag)
                .filter(tagEnum -> tagEnum != null && tagEnum.getKorean().equals("읽고있는책")).count();

        List<BookDTO> books = myShelf.stream()
                .map(BookShelfDTO::getBookDTO)
                .flatMap(book -> bookService.findAllByDTOId(book.getId()).stream())
                .toList();

        model.addAttribute("read", read);
        model.addAttribute("want", want);
        model.addAttribute("current",current);
        model.addAttribute("shelf", myShelf);
        model.addAttribute("books",books);
        return "book/myShelf";
    }

}
