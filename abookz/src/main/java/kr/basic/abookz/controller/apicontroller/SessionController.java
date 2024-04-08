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
        Long check = (Long)httpSession.getAttribute("id");
        System.out.println(check);

        if(httpSession.getAttribute("id") == null){
            String data="로그인부터해주세요";
            return data;
        }

            MemberDTO member = memberService.findById(check);
            BookDTO bookEntity = aladinService.getOneBookEntity(book);
            BookDTO  insertBook =bookService.insertBook(bookEntity);
            BookShelfDTO bookShelfEntity = BookShelfDTO
                    .builder()
                    .memberDTO(member)
                    .bookDTO(insertBook)
                    .build();
          String checkMyShelf =  bookShelfService.save(bookShelfEntity);
        String data = null;
            if(checkMyShelf==null) {
            data = bookEntity.getTitle() + " 책이 정상적으로 내 서재에 등록되었습니다.";
            return data;
            }
            data = "이미 내 서재에 등록되어 있습니다";
            return data;

    }
    @RequestMapping("/readingUpdate")
    public String readingUpdate(@RequestBody BookShelfDTO jsonData){
        System.out.println("jsonData = " + jsonData);
        return "check";
    }



}
