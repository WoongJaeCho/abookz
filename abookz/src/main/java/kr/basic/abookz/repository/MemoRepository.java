package kr.basic.abookz.repository;

import kr.basic.abookz.dto.MemoDTO;
import kr.basic.abookz.entity.review.CommentEntity;
import kr.basic.abookz.entity.review.MemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<MemoEntity, Long> {

    public int countDistinctByBookShelf_Id(Long bookshelfId);

    public MemoEntity findTopByBookShelf_IdOrderByPageDesc(Long bookshelfId);
    public List<MemoEntity> findAllByBookShelf_id(Long bookshelfId);
}
