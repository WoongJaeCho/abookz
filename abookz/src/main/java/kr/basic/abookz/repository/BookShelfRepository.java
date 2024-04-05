package kr.basic.abookz.repository;

import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.book.TagEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookShelfRepository extends JpaRepository<BookShelfEntity,Long> {
    List<BookShelfEntity> findAllByMemberId(Long memberId);
    List<BookShelfEntity> findAllByMemberIdAndTag(Long memberId,TagEnum tag);

}
