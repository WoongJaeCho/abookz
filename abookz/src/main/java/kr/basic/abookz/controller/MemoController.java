package kr.basic.abookz.controller;

import kr.basic.abookz.dto.MemoDTO;
import kr.basic.abookz.dto.admin.SlideCardDTO;
import kr.basic.abookz.service.BookShelfService;
import kr.basic.abookz.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/memo")
public class MemoController {

    private final BookShelfService bookShelfService;
    private final MemoService memoService;
    @PostMapping
    public String memo(MemoDTO memoDTO){
        System.out.println("memoDTO = " + memoDTO);
        memoService.save(memoDTO);
        return "redirect:/";
    }

}
