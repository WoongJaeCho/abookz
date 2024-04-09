package kr.basic.abookz.controller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.entity.book.TagEnum;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
      if (principalDetails == null) {
        return "/member/loginForm";
      }
      Long memId =  principalDetails.getMember().getId();
        List<BookShelfDTO> shelf =shelfService.findAllDTOByMemberId(memId);
       //밑에는 각 사이즈 가져오기 내서재들 옆 숫자표시 몇권있는지
        int read = (int) shelf.stream()
                .map(item -> item.getTag())
                .filter(tag -> tag != null && tag.getKorean().equals("읽은책"))
                .count();
        int want =(int)shelf.stream().map(item -> item.getTag())
                .filter(Objects::isNull).count();
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
    public String myShelfTag(@PathVariable ("tag")String  tag, Model model,@AuthenticationPrincipal PrincipalDetails principalDetails){
      if (principalDetails == null) {
        return "/member/loginForm";
      }
      Long memId =  principalDetails.getMember().getId();
        TagEnum tagValue =null;
        for(TagEnum tagEnum : TagEnum.values()){
                if(tagEnum.getKorean().equals(tag)){

                }
        }
        if(tag.equals("all")){
            List<BookShelfDTO> myShelf = shelfService.findAllDTOByMemberId(memId);
            return "book/myShelf";
        }else if(tag.equals("currently")){
            List<BookShelfDTO> myShelf = shelfService.findAllByMemberIdAndTag(memId,tagValue);
            return "book/myShelf";
        }else if(tag.equals("read")){
            List<BookShelfDTO> myShelf = shelfService.findAllByMemberIdAndTag(memId,tagValue);
            return "book/myShelf";
        }

        return"book/myShelf";
    }

}
