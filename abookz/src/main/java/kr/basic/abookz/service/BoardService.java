package kr.basic.abookz.service;

import kr.basic.abookz.dto.BoardDTO;
import kr.basic.abookz.entity.board.BoardEntity;
import kr.basic.abookz.entity.board.Category;
import kr.basic.abookz.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
  // 생성자 주입
  private final BoardRepository boardRepository;

  // 작성
  public void save(BoardDTO boardDTO) {
    BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
    System.out.println("boardEntity = " + boardEntity);
    boardRepository.save(boardEntity);
  }

  // 조회
  public List<BoardDTO> findAll() { // 전체조회
    List<BoardEntity> EntityList = boardRepository.findAll();
    List<BoardDTO> DTOList = new ArrayList<>();
    for(BoardEntity e : EntityList){
      DTOList.add(BoardDTO.toBoardDTO(e));
    }
    return DTOList;
  }

  @Transactional
  public void updateHits(Long id) { // 조회수업데이트
    boardRepository.updateHits(id);
  }

  public BoardDTO findById(Long id) { // 게시글한개조회
    Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
    if(optionalBoardEntity.isPresent()){
      BoardEntity boardEntity = optionalBoardEntity.get();
      return BoardDTO.toBoardDTO(boardEntity);
    }
    else {
      return null;
    }
  }

  public BoardDTO update(BoardDTO boardDTO) {
    BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
    boardRepository.save(boardEntity);
    return findById(boardDTO.getId());
  }

  public void delete(Long id) {
    boardRepository.deleteById(id);
  }

  public Page<BoardDTO> paging(Pageable pageable) {
    int page = pageable.getPageNumber() - 1;
    int pageLimit = 5; // 한 페이지에 보여줄 글 갯수
    Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

    System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
    System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
    System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
    System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
    System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
    System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
    System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
    System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

    // 목록 : id, writer, title, hits, creatDate
    return boardEntities.map(board -> new BoardDTO(board.getId(), board.getTitle(), board.getWriter(), board.getHits(), board.getCreateDate(), board.getCategory()));
  }

  public Page<BoardDTO> pagingCategory(Pageable pageable, Category category){
    int page = pageable.getPageNumber() - 1;
    int pageLimit = 5; // 한 페이지에 보여줄 글 갯수
    Page<BoardEntity> boardEntities = boardRepository.findAllByCategory(pageable, category, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

    System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
    System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
    System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
    System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
    System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
    System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
    System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
    System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

    return boardEntities.map(board -> new BoardDTO(board.getId(), board.getTitle(), board.getWriter(), board.getHits(), board.getCreateDate(), board.getCategory()));

  }
}
