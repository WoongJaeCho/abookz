package kr.basic.abookz.controller.apicontroller;

import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BoardCommentDTO;
import kr.basic.abookz.dto.BoardDTO;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.service.BoardCommentService;
import kr.basic.abookz.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@Slf4j
public class BoardCommentApiController {

        private final BoardCommentService boardCommentService;
        private  final MemberService memberService;

        @RequestMapping(value = "/board/comment", method = RequestMethod.GET)
        public ResponseEntity<Page<BoardCommentDTO>> getBoardComment(@RequestParam Long boardId
                                                                , @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue =  "10") int size){
                Page<BoardCommentDTO> boardComment = boardCommentService.getPageBoardCommentDTO(boardId,page,size);

                return ResponseEntity.ok(boardComment);
        }
        @RequestMapping(value = "/board/comment/delete", method = RequestMethod.POST)
        public RedirectView  getOneDeleteComment(@RequestBody BoardCommentDTO boardCommentDTO
                                                                        ,@AuthenticationPrincipal PrincipalDetails principalDetails){

            Long memberId = principalDetails.getMember().getId();

            String boardComment =  boardCommentService.getOneDelete(boardCommentDTO.getId(),memberId);
            String redirectUrl = "/board/" + boardCommentDTO.getBoardDTO().getId();
            return new RedirectView(redirectUrl);
        }
        @RequestMapping(value = "/board/comment/save" ,method =  RequestMethod.POST)
        public RedirectView getInsertBoardComment(@RequestBody BoardCommentDTO boardCommentDTO
                                        , @AuthenticationPrincipal PrincipalDetails principalDetails){

                if(principalDetails == null){

                        return  new RedirectView("/member/loginForm");
                }

                Long id   = principalDetails.getMember().getId();
             MemberDTO memberDTO = memberService.findById(id);
             boardCommentDTO = BoardCommentDTO.builder()
                     .memberDTO(memberDTO)
                     .boardDTO(boardCommentDTO.getBoardDTO())
                     .Comment(boardCommentDTO.getComment())
                     .build();
             boardCommentService.getOneSave(boardCommentDTO);
                String redirectUrl = "/board/" + boardCommentDTO.getBoardDTO().getId();
                return new RedirectView(redirectUrl);
        }
    @RequestMapping(value = "/board/comment/update" ,method = RequestMethod.POST)
    public RedirectView getOneCommentUpdate(@RequestBody BoardCommentDTO boardCommentDTO
                                ,@AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails == null){

            return  new RedirectView("/member/loginForm");
        }
        Long id   = principalDetails.getMember().getId();
        MemberDTO memberDTO = memberService.findById(id);
        System.out.println("memberDTO = " + memberDTO);
       BoardCommentDTO getBoardDTO = BoardCommentDTO.builder()
                .memberDTO(memberDTO)
                .id(boardCommentDTO.getId())
                .boardDTO(boardCommentDTO.getBoardDTO())
                .Comment(boardCommentDTO.getComment())
                .build();
        System.out.println("getBoardDTO빌더값 = " + getBoardDTO);
                boardCommentService.updateBoardComment(getBoardDTO);
        String redirectUrl = "/board/" + boardCommentDTO.getBoardDTO().getId();
        return new RedirectView(redirectUrl);
    }

}
