package kr.basic.abookz.controller;

import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.service.AladinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final AladinService aladinService;


    @GetMapping("/content/{isbn13}")
    public String getOneDetail(@PathVariable ("isbn13")String isbn13, Model model) throws Exception {
            BookDTO book= aladinService.searchGetOneItem(isbn13);
            model.addAttribute("book", book);

        return  "/book/detail";
    }
}
