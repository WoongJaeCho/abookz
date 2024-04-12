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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        List<SlideCardDTO> slideCardDTOs = slideCardService.findAllOrderByIdx();

        for (SlideCardDTO slideCardDTO : slideCardDTOs) {
            //List<BookDTO> byDTOISBN13 = bookService.findByDTOISBN13(slideCardDTO.getISBN13());
            String isbn13 = slideCardDTO.getISBN13()+"";
            BookDTO book = aladinService.searchGetOneItem(isbn13);
                slideCardDTO.setBook(book);
//            if(book.getTitle() != null){
//            }
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

    @PostMapping("/order")
    public String slideOrder(Model model, @RequestBody Map<String, String[]> slideIds){
        String[] slideIdsArray = slideIds.get("slideIds");
        List<SlideCardDTO> slideCardDTOs = slideCardService.findAll();
        System.out.println("slideIds = " + Arrays.toString(slideIdsArray));
        int idx = 1;
        for(String slide : slideIdsArray){
            slideCardDTOs.get(Integer.parseInt(slide)-1).setIdx(idx++);
        }
        for(SlideCardDTO slideCardDTO : slideCardDTOs){
            slideCardService.update(slideCardDTO);
        }
        return "redirect:/slide";
    }

}
