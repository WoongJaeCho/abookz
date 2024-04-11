package kr.basic.abookz.repository;

import kr.basic.abookz.entity.board.BoardEntity;
import kr.basic.abookz.entity.board.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
  // update BOARD set BOARD_hits = BOARD_hits + 1 where id = ?
  @Modifying
  @Query(value = "update BoardEntity b set b.hits=b.hits+1 where b.id=:id")
  void updateHits(@Param("id") Long id);

  List<BoardEntity> findByCategory(Category category);
  Page<BoardEntity> findAllByCategory(Pageable pageable, Category category, PageRequest pageRequest);
}
