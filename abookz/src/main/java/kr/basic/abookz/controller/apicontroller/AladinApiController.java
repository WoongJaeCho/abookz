package kr.basic.abookz.controller.apicontroller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookPagingDTO;
import kr.basic.abookz.service.AladinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AladinApiController {

    private final AladinService aladinService;
    private  final HttpSession session;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<Page<BookDTO>> search(@RequestBody BookPagingDTO bookPagingDTO) {
        try {
            session.setAttribute("q",bookPagingDTO.getQuery());
            Page<BookDTO>  books = aladinService.searchItems(bookPagingDTO.getQuery(), PageRequest.of(bookPagingDTO.getPage(), bookPagingDTO.getSize()));
            System.out.println("books = " + books);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PageImpl<>(new ArrayList<>()));
        }
    }
}
