package kr.basic.abookz.repository;

import kr.basic.abookz.entity.board.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
  // update BOARD set BOARD_hits = BOARD_hits + 1 where id = ?
  @Modifying
  @Query(value = "update BoardEntity b set b.hits=b.hits+1 where b.id=:id")
  void updateHits(@Param("id") Long id);

}
