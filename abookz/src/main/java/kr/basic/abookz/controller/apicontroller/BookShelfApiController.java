package kr.basic.abookz.controller.apicontroller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.service.AladinService;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import kr.basic.abookz.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BookShelfApiController {
    private final HttpSession httpSession;
    private final BookShelfService bookShelfService;
    private final MemberService memberService;
    private final BookService bookService;
    private final AladinService aladinService;

    @RequestMapping(value = "/want",method = RequestMethod.POST)
    public String wantToRead(@RequestParam("book") String book, Authentication authentication,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        Long id = principalDetails.getMember().getId();
        String data = null;
        if (id == null) {
            data = "로그인부터해주세요";
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
        if (getValue.equals("저장")) {
            data = aladinGetBook.getTitle() + "내 서재에 등록이 완료되었습니다";
            return data;
        }
        data = "이미 등록되어있습니다";
        return data;
    }
    @PostMapping("/readingUpdate")
    public ResponseEntity<Object> readingUpdate(@RequestBody BookShelfDTO jsonData) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        BookShelfDTO bookShelfDTO =
                BookShelfDTO.builder()
                        .id(jsonData.getId())
                        .tag(jsonData.getTag())
                        .startDate(now).build();
        bookShelfService.bookShelfUpdate(bookShelfDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/myshelf"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


}
