package kr.basic.abookz.controller;

import kr.basic.abookz.dto.BoardDTO;
import kr.basic.abookz.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
  // 생성자 주입
  private final BoardService boardService;

  // 글쓰기(작성)
  @GetMapping("/save")
  public String saveForm()
  {
    return "board/save";
  }
  @PostMapping("/save")
  public String save(@ModelAttribute BoardDTO boardDTO){
    boardService.save(boardDTO);
    return "redirect:/board/paging";
  }

  // 조회
  @GetMapping("/list")
  public String findAll(Model model){
    // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여줌
    List<BoardDTO> List = boardService.findAll();
    model.addAttribute("boardList", List);
    return "board/list";
  }
  @GetMapping("/{id}")
  public String findById(@PathVariable Long id, Model model, @PageableDefault(page = 1) Pageable pageable){
    // 해당 게시글의 조회수를 하나 올리고 게시글 데이터 가져와서 detail.html에 출력
    boardService.updateHits(id);
    BoardDTO boardDTO = boardService.findById(id);
    model.addAttribute("board", boardDTO);
    model.addAttribute("page", pageable.getPageNumber());
    return "board/detail";
  }

  // 수정
  @GetMapping("/update/{id}")
  public String updateForm(@PathVariable Long id, Model model){
    BoardDTO boardDTO = boardService.findById(id);
    model.addAttribute("boardUpdate", boardDTO);
    return "board/update";
  }
  @PostMapping("/update")
  public String update(@ModelAttribute BoardDTO boardDTO, Model model){
    BoardDTO board = boardService.update(boardDTO);
    model.addAttribute("board", board);
    return "board/detail";
  }

  // 삭제
  @GetMapping("/delete/{id}")
  public String deletePost(@PathVariable Long id){
    boardService.delete(id);
    return "redirect:/board/list";
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
}
