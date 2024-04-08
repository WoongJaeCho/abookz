package kr.basic.abookz.controller.admin;

import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.admin.SlideCardDTO;
import kr.basic.abookz.service.AladinService;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.admin.SlideCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SlideCardController {

    private final SlideCardService slideCardService;
    private final BookService bookService;

    private final AladinService aladinService;
    @GetMapping("/slide")
    public String Slide(Model model) throws Exception {
        List<SlideCardDTO> slideCardDTOs = slideCardService.findAll();

        for (SlideCardDTO slideCardDTO : slideCardDTOs) {
            //List<BookDTO> byDTOISBN13 = bookService.findByDTOISBN13(slideCardDTO.getISBN13());
            String isbn13 = slideCardDTO.getISBN13()+"";
            BookDTO book = aladinService.searchGetOneItem(isbn13);
            slideCardDTO.setBook(book);
        }
        for (SlideCardDTO slideCardDTO : slideCardDTOs){
            System.out.println("slideCardDTO = " + slideCardDTO);
        }
        
        model.addAttribute("slideCard", slideCardDTOs);
        return "admin/slideCard";
    }

}
