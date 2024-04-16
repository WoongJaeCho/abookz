package kr.basic.abookz.controller;

import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.MemoDTO;
import kr.basic.abookz.entity.book.TagEnum;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import kr.basic.abookz.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/memo")
public class MemoController {
    private final BookService bookService;
    private final BookShelfService bookShelfService;
    private final MemoService memoService;
    @PostMapping
    public String memo(MemoDTO memoDTO){
        System.out.println("memoDTO = " + memoDTO);
        memoService.save(memoDTO);
        return "redirect:/";
    }
    @GetMapping("/all")
    public String memoAll(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long memId = principalDetails.getMember().getId();
        List<BookShelfDTO> shelves = bookShelfService.findAllDTOByMemberId(memId);
        List<MemoDTO> memos = memoService.memoLastOne(memId);
        int[] memoCount = memoService.memoCount(memId);
        System.out.println("memoCount = " + memoCount);
        if(memos.size() != 0){
            model.addAttribute("memos", memos);
        }
//        List<BookShelfDTO> shelf =shelfService.findAllDTOByMemberId(id);

        //밑에는 각 사이즈 가져오기 내서재들 옆 숫자표시 몇권있는지
        int read = (int) shelves.stream()
                .map(item -> item.getTag())
                .filter(tag -> tag != null && tag.getKorean().equals("읽은책"))
                .count();
        int want = (int) shelves.stream()
                .map(BookShelfDTO::getTag)
                .filter(tag -> tag == null || tag == TagEnum.READ)
                .count();
        int current=(int)shelves.stream().map(item -> item.getTag())
                .filter(tag -> tag != null && tag.getKorean().equals("읽고있는책")).count();

        List<BookDTO> books = shelves.stream()
                .map(BookShelfDTO::getBookDTO)
                .flatMap(book -> bookService.findAllByDTOId(book.getId()).stream())
                .toList();
        //숫자 카운터 보내기용
        model.addAttribute("read", read);
        model.addAttribute("want", want);
        model.addAttribute("current",current);

        model.addAttribute("books",books);

        model.addAttribute("shelves", shelves);
        model.addAttribute("memoCount", memoCount);
        return "review/memoAllView";

    }

    @GetMapping("/{id}")
    public String memoOne(Model model, @PathVariable("id") Long bookshelfId,@AuthenticationPrincipal PrincipalDetails principalDetails){

        List<MemoDTO> memos = memoService.findAllbyId(bookshelfId);

        model.addAttribute("memos", memos);

        Long memId = principalDetails.getMember().getId();
        List<BookShelfDTO> shelves = bookShelfService.findAllDTOByMemberId(memId);
        int[] memoCount = memoService.memoCount(memId);
        System.out.println("memoCount = " + memoCount);
        if(memos.size() != 0){
            model.addAttribute("memos", memos);
        }
//        List<BookShelfDTO> shelf =shelfService.findAllDTOByMemberId(id);

        //밑에는 각 사이즈 가져오기 내서재들 옆 숫자표시 몇권있는지
        int read = (int) shelves.stream()
                .map(item -> item.getTag())
                .filter(tag -> tag != null && tag.getKorean().equals("읽은책"))
                .count();
        int want = (int) shelves.stream()
                .map(BookShelfDTO::getTag)
                .filter(tag -> tag == null || tag == TagEnum.READ)
                .count();
        int current=(int)shelves.stream().map(item -> item.getTag())
                .filter(tag -> tag != null && tag.getKorean().equals("읽고있는책")).count();

        List<BookDTO> books = shelves.stream()
                .map(BookShelfDTO::getBookDTO)
                .flatMap(book -> bookService.findAllByDTOId(book.getId()).stream())
                .toList();
        //숫자 카운터 보내기용
        model.addAttribute("read", read);
        model.addAttribute("want", want);
        model.addAttribute("current",current);

        model.addAttribute("books",books);

        model.addAttribute("shelves", shelves);
        model.addAttribute("memoCount", memoCount);

        return "review/memoOneView";
    }

}



