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
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookShelfController {

    private final BookShelfService shelfService;
    private final BookService bookService;
    //   내 서재로 가기
    @GetMapping("/myshelf")
    public String getMyShelf(RedirectAttributes redirectAttributes, Model model
            , @AuthenticationPrincipal PrincipalDetails principalDetails,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size  ){
        Long id = principalDetails.getMember().getId();
        List<BookShelfDTO> myShelf =shelfService.findAllDTOByMemberIdOrderByIdDesc(id);
        Slice<BookShelfDTO> myShelfSlice = shelfService.SliceBookShelfDTO(id,page,size);

       //밑에는 각 사이즈 가져오기 내서재들 옆 숫자표시 몇권있는지
        int read = (int) myShelf.stream()
                .map(item -> item.getTag())
                .filter(tag -> tag != null && tag.getKorean().equals("읽은 책"))
                .count();
        int want = (int) myShelf.stream()
                .map(BookShelfDTO::getTag)
                .filter(tag -> tag == null || tag == TagEnum.READ)
                .count();
        int current=(int)myShelf.stream().map(item -> item.getTag())
                .filter(tag -> tag != null && tag.getKorean().equals("읽고있는 책")).count();


        List<BookDTO> books = myShelf.stream()
                .map(BookShelfDTO::getBookDTO)
                .flatMap(book -> bookService.findAllByIdOrderByIdDesc(book.getId()).stream())
                .toList();
        System.out.println("books = " + books);
        //숫자 카운터 보내기용
        model.addAttribute("read", read);
        model.addAttribute("want", want);
        model.addAttribute("current",current);
        model.addAttribute("all",myShelf);
        model.addAttribute("shelf",myShelf);
        model.addAttribute("currentPage",page);
        model.addAttribute("isLastPage", myShelfSlice.isLast());
        model.addAttribute("shelfSlice",myShelfSlice);
        model.addAttribute("books",books);
        return "book/myShelf";
    }
    @GetMapping("/myshelf/tag/{tag}")
    public String myShelfTag(@PathVariable ("tag")String  tag, Model model,   @AuthenticationPrincipal PrincipalDetails principalDetails,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size){
        Long memId= principalDetails.getMember().getId();

        System.out.println("tag.toUpperCase() = " + tag.toUpperCase());
        List<BookShelfDTO> myShelf;
        List<BookShelfDTO> count = shelfService.findAllDTOByMemberIdOrderByIdDesc(memId);
        Slice<BookShelfDTO> myShelfSlice;
        if (tag.equals("ALL")) {
            myShelf = shelfService.findAllDTOByMemberIdOrderByIdDesc(memId);
            myShelfSlice =shelfService.SliceBookShelfDTO(memId,page,size);
        } else {
            try {
                TagEnum tagEnum = TagEnum.valueOf(tag.toUpperCase());
                myShelf = shelfService.findAllByMemberIdAndTagOrderByIdDesc(memId,tagEnum);
                myShelfSlice =shelfService.SliceBookShelfDTOIdAndTag(memId,tagEnum,page,size);
            } catch (IllegalArgumentException e) {
                // 태그가 유효하지 않은 경우의 처리
                return "errorPage"; // 적절한 에러 페이지로 리다이렉트
            }

        }

        int read = (int) count.stream()
                .map(BookShelfDTO::getTag)
                .filter(tagEnum -> tagEnum != null && tagEnum.getKorean().equals("읽은 책"))
                .count();
        int want = (int) count.stream()
                .map(BookShelfDTO::getTag)
                .filter(tagEnum -> tagEnum == null || tagEnum.getKorean().equals("읽고싶은 책"))
                .count();
        int current=(int)count.stream().map(BookShelfDTO::getTag)
                .filter(tagEnum -> tagEnum != null && tagEnum.getKorean().equals("읽고있는 책")).count();

        List<BookDTO> books = myShelf.stream()
                .map(BookShelfDTO::getBookDTO)
                .flatMap(book -> bookService.findAllByIdOrderByIdDesc(book.getId()).stream())
                .toList();
        System.out.println("books = " + books);
        model.addAttribute("tagValue", tag);
        model.addAttribute("read", read);
        model.addAttribute("want", want);
        model.addAttribute("current",current);
        model.addAttribute("all",myShelf);
        model.addAttribute("shelf",myShelf);
        model.addAttribute("currentPage",page);
        model.addAttribute("isLastPage", myShelfSlice.isLast());
        model.addAttribute("shelfSlice",myShelfSlice);
        model.addAttribute("books",books);
        return "book/myShelf";
    }


}
