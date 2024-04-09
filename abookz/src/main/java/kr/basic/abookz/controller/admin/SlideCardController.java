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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/slide")
public class SlideCardController {

    private final SlideCardService slideCardService;
    private final BookService bookService;

    private final AladinService aladinService;
    @GetMapping
    public String Slide(Model model) throws Exception {
        List<SlideCardDTO> slideCardDTOs = slideCardService.findAll();

        for (SlideCardDTO slideCardDTO : slideCardDTOs) {
            //List<BookDTO> byDTOISBN13 = bookService.findByDTOISBN13(slideCardDTO.getISBN13());
            String isbn13 = slideCardDTO.getISBN13()+"";
            BookDTO book = aladinService.searchGetOneItem(isbn13);
            if(book.getTitle() != null){
                slideCardDTO.setBook(book);
            }
        }
        model.addAttribute("slideCard", slideCardDTOs);
        return "admin/slideCard";
    }

    @PostMapping
    public String uploadSlide(@ModelAttribute SlideCardDTO slideCardDTO, @RequestParam("file") MultipartFile file){
        System.out.println("slideCardDTO = " + slideCardDTO + ", file = " + file);
        try {
            slideCardService.upload(slideCardDTO, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/slide";
    }

    @GetMapping("/{id}")
    public String deleteCard(@PathVariable("id")Long id){
        slideCardService.deletebyId(id);
        return "redirect:/slide";
    }
}
