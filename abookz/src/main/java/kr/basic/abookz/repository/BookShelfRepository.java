package kr.basic.abookz.repository;

import kr.basic.abookz.entity.book.BookShelfEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookShelfRepository extends JpaRepository<BookShelfEntity,Long> {
    List<BookShelfEntity> findAllByMemberId(Long memberId);
}
