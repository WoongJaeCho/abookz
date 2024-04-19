package kr.basic.abookz.controller.apicontroller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.entity.book.CategoryEnum;
import kr.basic.abookz.entity.book.TagEnum;
import kr.basic.abookz.service.AladinService;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import kr.basic.abookz.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Slice;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BookShelfApiController {
    private final BookShelfService bookShelfService;
    private final MemberService memberService;
    private final BookService bookService;
    private final AladinService aladinService;

    @RequestMapping(value = "/want", method = RequestMethod.POST)
    public String wantToRead(@RequestParam("book") String book, Authentication authentication,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        System.out.println("book = " + book);
        Long id = principalDetails.getMember().getId();
        String data = null;
        MemberDTO memberDTO = memberService.findById(id);
        BookDTO aladinGetBook = aladinService.getOneBookDTO(book);
        BookShelfDTO bookShelfDTO = BookShelfDTO.builder()
                .memberDTO(memberDTO)
                .bookDTO(aladinGetBook)
                .build();
        String getValue = bookShelfService.insertBookAndBookShelf(aladinGetBook, bookShelfDTO);
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
        System.out.println("jsonData.toString() = " + jsonData.toString());
        if (jsonData.getTag() != null && jsonData.getTag().toString() != null) {
            BookShelfDTO bookShelfDTO =
                    BookShelfDTO.builder()
                            .id(jsonData.getId())
                            .tag(jsonData.getTag())
                            .build();
            bookShelfService.bookShelfUpdateTag(bookShelfDTO);
        } else {
            BookShelfDTO bookShelfDTO = BookShelfDTO
                    .builder()
                    .id(jsonData.getId())
                    .endDate(jsonData.getEndDate())
                    .targetDate(jsonData.getTargetDate())
                    .currentPage(jsonData.getCurrentPage())
                    .build();
            bookShelfService.bookShelfUpdateDate(bookShelfDTO);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/myshelf"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    //나의 서재 삭제하기
    @PostMapping("/deleteMyShelf")
    public ResponseEntity<Object> deleteMyShelf(@RequestBody BookShelfDTO jsonData, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getId();
        System.out.println("memberId = " + memberId);
        Long id = jsonData.getId();
        System.out.println("Checkid = " + id);
        if (id == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/myshelf"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        bookShelfService.deleteBookShelf(id, memberId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/myshelf"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @RequestMapping(value = "/modal", method = RequestMethod.POST)
    public Map<String, Object> modalGetBookOneDetail(@RequestParam("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        BookShelfDTO bookShelfDTO = null;
        bookShelfDTO = bookShelfService.findByBookShelfId(id);
        BookDTO bookDTO = bookService.findByBookId(bookShelfDTO.getBookDTO().getId());
        response.put("bookShelfDTO", bookShelfDTO);
        response.put("bookDTO", bookDTO);
        return response;
    }

    @GetMapping("/myshelfSlice")
    public ResponseEntity<Slice<BookShelfDTO>> getMyShelf(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size
            , @RequestParam String tag
            , @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getId();
        if (tag.equals("null")) {

            Slice<BookShelfDTO> bookShelfDTO = bookShelfService.SliceBookShelfDTO(memberId, page, size);
            System.out.println("bookShelfDTO = " + bookShelfDTO);
            return ResponseEntity.ok(bookShelfDTO);
        }
            TagEnum tagEnum = TagEnum.valueOf(tag.toUpperCase());
            Slice<BookShelfDTO> bookShelfDTO = bookShelfService.SliceBookShelfDTOIdAndTag(memberId, tagEnum, page, size);
            return ResponseEntity.ok(bookShelfDTO);
    }
}


