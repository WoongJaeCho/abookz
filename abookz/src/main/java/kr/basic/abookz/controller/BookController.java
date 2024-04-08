package kr.basic.abookz.controller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.service.AladinService;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import kr.basic.abookz.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final AladinService aladinService;
    private final BookShelfService shelfService;
    private  final BookService bookService;

    @GetMapping("/search")
    public String getSearh(){
        return"/index";
    }

    @GetMapping("/content/{isbn13}")
    public String getOneDetail(@PathVariable ("isbn13")String isbn13, Model model) throws Exception {
            BookDTO book= aladinService.searchGetOneItem(isbn13);
            model.addAttribute("book", book);
            System.out.println("book = " + book);
        return  "/book/detail";
    }
    @GetMapping("/category/{category}")
    public String choiceCategory(@PathVariable ("category")String category,Model model) throws Exception {
        List<BookDTO>  getCategoryList = aladinService.choiceGetCategoryList(category);
        model.addAttribute("books", getCategoryList);
        System.out.println("getCategoryList = " + getCategoryList);
        return "book/category";
    }
    /* 베스트 셀러나 신간 리스트 가져오기*/
    @GetMapping("/bestSeller/{type}")
    public String choiceQueryType(@PathVariable ("type")String type,Model model) throws Exception {
           List<BookDTO> queryTypeList = aladinService.getQueryTypeList(type);
        model.addAttribute("books",queryTypeList);
        System.out.println("getQueryTypeList = " + queryTypeList);
        return "book/queryType";
    }
}
