package kr.basic.abookz.controller.apicontroller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.dto.BookDTO;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SessionController {
    private final HttpSession httpSession;
    private final BookShelfService bookShelfService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
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

            MemberEntity member = memberRepository.findById(check).get();
        System.out.println();
            BookEntity bookEntity = aladinService.getOneBookEntity(book);
            bookService.insertBook(bookEntity);
            BookShelfEntity bookShelfEntity = BookShelfEntity
                    .builder()
                    .member(member)
                    .book(bookEntity).
                    build();
            bookShelfService.save(bookShelfEntity);
            String data = bookEntity.getTitle() + " 책이 정상적으로 내 서재에 등록되었습니다.";

            return data;

    }



}
