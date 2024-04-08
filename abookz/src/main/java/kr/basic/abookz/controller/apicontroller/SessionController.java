package kr.basic.abookz.controller.apicontroller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.repository.BookShelfRepository;
import kr.basic.abookz.repository.MemberRepository;
import kr.basic.abookz.service.AladinService;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import kr.basic.abookz.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SessionController {
    private final HttpSession httpSession;
    private final BookShelfService bookShelfService;
    private final MemberService memberService;
    private final BookService bookService;
    private final AladinService aladinService;

    @RequestMapping(value = "/want",method = RequestMethod.POST)
    public String wantToRead(@RequestParam("book") String book ,RedirectAttributes redirectAttributes) throws Exception {
        Long id = (Long)httpSession.getAttribute("id");
        String data = null;

        if(httpSession.getAttribute("id") == null){
             data="로그인부터해주세요";
            return data;
        }
            MemberDTO memberDTO = memberService.findById(id);
            BookDTO aladinGetBook = aladinService.getOneBookDTO(book);
            BookDTO checkBook = bookService.insertBook(aladinGetBook);
            BookShelfDTO bookShelfDTO = BookShelfDTO.builder()
                    .memberDTO(memberDTO)
                    .bookDTO(checkBook).build();
            String getValue = bookShelfService.insertBookShelfCheck(bookShelfDTO);
        System.out.println(getValue);
            if(getValue.equals("저장")){
                data= aladinGetBook.getTitle() +"내 서재에 등록이 완료되었습니다";
                return data;
            }
            data="이미 등록되어있습니다";
        return  data;
    }
    @RequestMapping("/readingUpdate")
    public String readingUpdate(@RequestBody BookShelfDTO jsonData){
        System.out.println("jsonData = " + jsonData);
        return "check";
    }



}
