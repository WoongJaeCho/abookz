package kr.basic.abookz.controller.apicontroller;

import jakarta.servlet.http.HttpSession;

import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.entity.BookEntity;
import kr.basic.abookz.service.AladinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
public class AladinApiController {

    private AladinService aladinService;


    @Autowired
    public AladinApiController(AladinService aladinService){
        this.aladinService = aladinService;
    }


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<BookDTO>  search(@RequestParam("query") String query,  RedirectAttributes redirectAttributes) {
        List<BookDTO> books = null;
        System.out.println("query" + query);
        try {
            books = aladinService.searchItems(query);
            System.out.println(books.toString());
            return books;
         } catch (Exception e) {
            // 실제 환경에서는 예외 처리를 보다 세심하게 해야 합니다.
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "검색결과가 존재하지 않습니다");
            return books;
        }
    }
}
