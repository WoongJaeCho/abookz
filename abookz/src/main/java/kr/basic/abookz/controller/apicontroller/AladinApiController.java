package kr.basic.abookz.controller.apicontroller;

import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.service.AladinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AladinApiController {

    private final AladinService aladinService;


    @PostMapping(value = "/search")
    public List<BookDTO>  search(@RequestParam("query") String query,  RedirectAttributes redirectAttributes) {
        List<BookDTO> books = null;
        System.out.println("query" + query);
        try {
            books = aladinService.searchItems(query);
            return books;
          } catch (Exception e) {
            // 실제 환경에서는 예외 처리를 보다 세심하게 해야 합니다.
            e.printStackTrace();
            return books;
        }
    }
}
