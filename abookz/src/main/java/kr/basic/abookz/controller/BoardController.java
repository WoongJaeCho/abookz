package kr.basic.abookz.controller;

import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BoardDTO;
import kr.basic.abookz.entity.board.Category;
import kr.basic.abookz.entity.member.RoleEnum;
import kr.basic.abookz.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
  // 생성자 주입
  private final BoardService boardService;

  public boolean logincheck(@AuthenticationPrincipal PrincipalDetails principalDetails){
    if(principalDetails == null){
      return false;
    }
    return true;
  }

  // 글쓰기(작성)
  @GetMapping("/save")
  public String saveForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
    if(!logincheck(principalDetails)){
        return "redirect:/member/loginForm";
    }
    model.addAttribute("writer", principalDetails.getMember().getName());
    String roleValue = String.valueOf(principalDetails.getMember().getRole());
    model.addAttribute("role" , roleValue);
    return "board/save";
  }
  @PostMapping("/save")
  public String save(@ModelAttribute BoardDTO boardDTO){
    System.out.println("boarddto = " + boardDTO);
    boardService.save(boardDTO);
    return "redirect:/board/paging";
  }

  // 조회 (페이징 완료에 따라 쓰이지 않음)
//  @GetMapping("/list")
//  public String findAll(Model model){
//    // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여줌
//    List<BoardDTO> List = boardService.findAll();
//    System.out.println("List =" + List);
//    model.addAttribute("boardList", List);
//    return "board/list";
//  }
  @GetMapping("/{id}")
  public String findById(@PathVariable Long id, Model model, @PageableDefault(page = 1) Pageable pageable, @AuthenticationPrincipal PrincipalDetails principalDetails){
    // 해당 게시글의 조회수를 하나 올리고 게시글 데이터 가져와서 detail.html에 출력
    boardService.updateHits(id);
    BoardDTO boardDTO = boardService.findById(id);
    model.addAttribute("board", boardDTO);
    model.addAttribute("page", pageable.getPageNumber());
    if(principalDetails != null){
      model.addAttribute("writer", principalDetails.getMember().getName());
      model.addAttribute("role",principalDetails.getMember().getRole());
    }
    else{
      model.addAttribute("writer", null);
    }
    return "board/detail";
  }

  // 수정
  @GetMapping("/update/{id}")
  public String updateForm(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long id, Model model){
    if(!logincheck(principalDetails)){
      return "redirect:/member/loginForm";
    }
    BoardDTO boardDTO = boardService.findById(id);
    model.addAttribute("boardUpdate", boardDTO);
    return "board/update";
  }
  @PostMapping("/update")
  public String update(@ModelAttribute BoardDTO boardDTO, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
    System.out.println(boardDTO);
    BoardDTO board = boardService.update(boardDTO);
    model.addAttribute("board", board);
    model.addAttribute("writer", principalDetails.getMember().getName());
    return "board/detail";
  }

  // 삭제
  @GetMapping("/delete/{id}")
  public String deletePost(@AuthenticationPrincipal PrincipalDetails principalDetails ,@PathVariable Long id){
    if(!logincheck(principalDetails)){
      return "member/loginForm";
    }
    boardService.delete(id);
    return "redirect:/board/paging";
  }

  // 페이징
  @GetMapping("/paging")
  public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){
    Page<BoardDTO> boardList = boardService.paging(pageable);
    int blockLimit = 5;
    int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
    int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

    model.addAttribute("boardList", boardList);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

    return "board/paging";
  }

  @GetMapping("/paging/{category}") // 카테고리별 분류
  public String pagingCategory(@PageableDefault(page = 1) Pageable pageable, @PathVariable String category, Model model){
    System.out.println("category = " + category);

    Category cate;
    try{
      cate = Category.valueOf(category.toUpperCase());
    }
    catch (IllegalArgumentException e){
      System.out.println("오류발생");
      cate = Category.FREE;
    }

    System.out.println("cate = " + cate);
    Page<BoardDTO> boardList = boardService.pagingCategory(pageable, cate);
    int blockLimit = 5;
    int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
    int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

    model.addAttribute("category", cate);
    model.addAttribute("boardList", boardList);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

    return "board/paging";
  }
}
