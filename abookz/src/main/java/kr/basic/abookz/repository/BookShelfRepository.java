package kr.basic.abookz.repository;

import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.book.TagEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public interface BookShelfRepository extends JpaRepository<BookShelfEntity,Long> {


    List<BookShelfEntity> findAllByMemberId(Long memberId);
    BookShelfEntity findByMemberIdAndBookId(Long memberId,Long bookId);
    BookShelfEntity findByIdAndMemberId(Long Id,Long memberId);
    List<BookShelfEntity> findAllByMemberIdOrderByIdDesc(Long memberId);
    List<BookShelfEntity> findAllByMemberIdAndTag(Long memberId, TagEnum tagEnum);

    List<BookShelfEntity> findAllByMemberIdAndTagOrderByIdDesc(Long memberId, TagEnum tagEnum);

    void deleteBookShelfEntityById(Long Id);
    Optional<BookShelfEntity> findByIdAndBookId(Long id, Long bookId);


}

