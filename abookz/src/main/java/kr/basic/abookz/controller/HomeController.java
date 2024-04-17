package kr.basic.abookz.controller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BoardDTO;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.admin.SlideCardDTO;
import kr.basic.abookz.entity.board.BoardEntity;
import kr.basic.abookz.service.AladinService;
import kr.basic.abookz.service.BoardService;
import kr.basic.abookz.service.BookService;
import kr.basic.abookz.service.BookShelfService;
import kr.basic.abookz.service.admin.SlideCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kr.basic.abookz.entity.board.Category.*;
import static kr.basic.abookz.entity.book.TagEnum.CURRENTLY_READING;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SlideCardService adminService;
    private final BookShelfService bookShelfService;
    private final BookService bookService;
    private final BoardService boardService;
    private final AladinService aladinService;

    @GetMapping("/")
    public String index(Model model, HttpSession session, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {

        // @@ slide
        List<SlideCardDTO> slideCard = adminService.findAllOrderByIdx();

        if( slideCard.size() != 0) {
            model.addAttribute("slideCard", slideCard);
        }


        // @@ notice
        List<BoardDTO> boards = new ArrayList<>();
        List<BoardDTO> boardEvent = boardService.findByCategory(EVENT);
        List<BoardDTO> boardNotice = boardService.findByCategory(NOTICE);
        List<BoardDTO> boardFree = boardService.findByCategory(FREE);

        if(boardEvent.size() != 0 && boardNotice.size() != 0 && boardFree.size() >= 4) {
            boards.add(boardEvent.get(0));
            boards.add(boardNotice.get(0));
            boards.add(boardFree.get(0));
            boards.add(boardFree.get(1));
            boards.add(boardFree.get(2));
            boards.add(boardFree.get(3));
        }
        if(boardEvent.size() == 0 && boardNotice.size() != 0 && boardFree.size() >= 5){
            boards.add(boardNotice.get(0));
            boards.add(boardFree.get(0));
            boards.add(boardFree.get(1));
            boards.add(boardFree.get(2));
            boards.add(boardFree.get(3));
            boards.add(boardFree.get(4));
        }
        if(boardEvent.size() != 0 && boardNotice.size() == 0 && boardFree.size() >= 5){
            boards.add(boardEvent.get(0));
            boards.add(boardFree.get(0));
            boards.add(boardFree.get(1));
            boards.add(boardFree.get(2));
            boards.add(boardFree.get(3));
            boards.add(boardFree.get(4));
        }
        if(boardEvent.size() == 0 && boardNotice.size() == 0 && boardFree.size() >= 6){
            boards.add(boardFree.get(0));
            boards.add(boardFree.get(1));
            boards.add(boardFree.get(2));
            boards.add(boardFree.get(3));
            boards.add(boardFree.get(4));
            boards.add(boardFree.get(5));
        }
        if(boards.size() == 6){
            for(BoardDTO board : boards){
                System.out.println("board = " + board);
            }
            model.addAttribute("boards", boards);
        }

        // @@ recommend
            // DB데이터 없을 경우 알라딘 베스트 셀러 출력
        List<BookDTO> queryTypeList = aladinService.getQueryTypeList("bestSeller");
        List<BookDTO> aladinBooks = queryTypeList.subList(0, 3);
        model.addAttribute("aladinBooks", aladinBooks);

        // @@ memo
        if(principalDetails != null) {
            Long memId = principalDetails.getMember().getId();

            List<BookShelfDTO> shelves = bookShelfService.findAllByMemberIdAndTag(memId, CURRENTLY_READING);
            List<BookDTO> books = shelves.stream()
                    .map(BookShelfDTO::getBookDTO)
                    .flatMap(book -> bookService.findAllByDTOId(book.getId()).stream())
                    .toList();

            System.out.println("books = " + books);

            for (BookShelfDTO shelf : shelves) {
                Duration duration = Duration.between(shelf.getStartDate(), LocalDateTime.now());
                shelf.setDays(duration.toDays());
                System.out.println("shelf = " + shelf);
            }

            if (shelves.size() != 0 && books.size() != 0) {
//                int curIdx = 0;
//                model.addAttribute("curIdx", curIdx);
                model.addAttribute("books", books);
                model.addAttribute("shelves", shelves);
            }
        }

        return "index";
    }

}
