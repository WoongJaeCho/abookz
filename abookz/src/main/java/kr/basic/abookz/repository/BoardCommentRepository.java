package kr.basic.abookz.repository;

import jakarta.persistence.Entity;
import kr.basic.abookz.entity.board.BoardCommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardCommentEntity, Long> {

    Page<BoardCommentEntity> findAllByBoardId(Long boardId,Pageable pageable);
    Page<BoardCommentEntity> findAllByMemberId(Long memberId,Pageable pageable);
    boolean existsByBoardId(Long boardId);
    BoardCommentEntity findByIdAndMemberId(Long id,Long memberId);
    void  deleteBoardCommentEntityById(Long id);
}
