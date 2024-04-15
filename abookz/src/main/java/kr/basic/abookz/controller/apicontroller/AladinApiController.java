package kr.basic.abookz.controller.apicontroller;

import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookPagingDTO;
import kr.basic.abookz.service.AladinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AladinApiController {

    private final AladinService aladinService;


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Page<BookDTO> search(@RequestParam("query") String query,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        try {
            List<BookDTO> books = aladinService.searchItems(query);
            return new PageImpl<>(books, PageRequest.of(page, size), books.size());
        } catch (Exception e) {
            e.printStackTrace();
            return new PageImpl<>(new ArrayList<>());
        }
    }
}
