package kr.basic.abookz.service;

import jakarta.persistence.EntityNotFoundException;
import kr.basic.abookz.dto.*;
import kr.basic.abookz.entity.board.BoardCommentEntity;
import kr.basic.abookz.entity.board.BoardEntity;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.repository.BoardCommentRepository;
import kr.basic.abookz.repository.BoardRepository;
import kr.basic.abookz.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardCommentService {

    private final BoardCommentRepository commentRepository;
    private final ModelMapper mapper;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    public Page<BoardCommentDTO> getPageBoardCommentDTO(Long boardId, int page , int size){
            if (boardId == null || !commentRepository.existsByBoardId(boardId)) {
                return Page.empty();
            }
        Pageable pageable = PageRequest.of(page,size);
        Page<BoardCommentEntity>  boardCommentEntities = commentRepository.findAllByBoardId(boardId,pageable);
        System.out.println("boardCommentEntities = " + boardCommentEntities);    
        Page<BoardCommentDTO> boardCommentDTOPage =   boardCommentEntities.map(this::mapEntityToDTO);
        System.out.println("boardCommentDTOPage = " + boardCommentDTOPage);
        return boardCommentDTOPage;
    }

    public String getOneDelete(Long id, Long memberId){
      BoardCommentEntity boardCommentEntity = commentRepository.findByIdAndMemberId(id, memberId);
        System.out.println("boardCommentEntity = " + boardCommentEntity);
      if(boardCommentEntity == null){
          
          return "잘못된접근";
      }
      commentRepository.deleteBoardCommentEntityById(boardCommentEntity.getId());
        System.out.println("삭제체크");
      return "성공";
    }



     public  String getOneSave(BoardCommentDTO boardCommentDTO){
         String comment = boardCommentDTO.getComment();
         Long boardId = boardCommentDTO.getBoardDTO().getId();
         Long memberId =boardCommentDTO.getMemberDTO().getId();
         LocalDateTime now = LocalDateTime.now();
         BoardEntity boardEntity = boardRepository.findById(boardId)
                 .orElseThrow(() -> new EntityNotFoundException("해당하는 BoardEntity를 찾을 수 없습니다. boardId: " + boardId));
         BoardCommentEntity boardCommentEntity = mapDTOToEntity(boardCommentDTO);
         Optional<MemberEntity> memberEntityOptional =  memberRepository.findById(memberId);
         MemberEntity memberEntity = memberEntityOptional.orElse(null);
         if(memberEntity == null){
             return "잘못된 접근";
         }

         boardCommentEntity = BoardCommentEntity.
                 builder().createdDate(now)
                        .comment(comment)
                         .board(boardEntity)
                        .member(memberEntity)
                 .build();
         commentRepository.save(boardCommentEntity);

         return "성공";
     }
    public String updateBoardComment(BoardCommentDTO boardCommentDTO){
     BoardCommentEntity boardCommentEntity  =  commentRepository.findByIdAndMemberId(boardCommentDTO.getId(),boardCommentDTO.getMemberDTO().getId());
    if(boardCommentEntity == null){
        return "실패";
    }
    BoardCommentEntity boardCommentChange = mapDTOToEntity(boardCommentDTO);
        boardCommentEntity.setComment(boardCommentChange.getComment());
    return  "체크";
    }







    public BoardCommentDTO mapEntityToDTO(BoardCommentEntity entity) {
        BoardCommentDTO boardCommentDTO = mapper.map(entity, BoardCommentDTO.class);
        BoardDTO boardDTO = mapper.map(entity.getBoard() , BoardDTO.class);
        MemberDTO memberDTO = mapper.map(entity.getMember(), MemberDTO.class);
        boardCommentDTO.setBoardDTO(boardDTO);
        boardCommentDTO.setMemberDTO(memberDTO);
        return boardCommentDTO;
    }

    public BoardCommentEntity mapDTOToEntity(BoardCommentDTO boardCommentDTO) {
        BoardCommentEntity boardCommentEntity = mapper.map(boardCommentDTO, BoardCommentEntity.class);
        if(boardCommentDTO.getBoardDTO()!= null){
            BoardEntity boardEntity = mapper.map(boardCommentDTO.getBoardDTO() , BoardEntity.class);
            boardCommentEntity.setBoard(boardEntity);
        }
        if(boardCommentDTO.getMemberDTO()!= null) {
            MemberEntity memberEntity = mapper.map(boardCommentDTO.getMemberDTO(), MemberEntity.class);
            boardCommentEntity.setMember(memberEntity);
        }
        return boardCommentEntity;
    }
}
