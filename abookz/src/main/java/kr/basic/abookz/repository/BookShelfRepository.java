package kr.basic.abookz.repository;

import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.book.TagEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    Optional<BookShelfEntity> findByMember_IdAndBook_ISBN13(Long id, Long bookId);
    Slice<BookShelfEntity> findAllByMemberIdOrderByIdDesc(Long memberId, Pageable pageable);

    @Query("SELECT b.member.id, SUM(b.book.weight) FROM BookShelfEntity b WHERE b.tag = 'READ' GROUP BY b.member.id")
    List<Object[]> findTotalWeightByMemberForReadBooks();
    Slice<BookShelfEntity> findAllByMemberIdAndTagOrderByIdDesc(Long memberId,TagEnum tagEnum,Pageable pageable);
    @Query("SELECT AVG(b.book.weight) FROM BookShelfEntity b WHERE b.member.id != :memberId AND b.tag = :tag")
    Double findAverageWeightExcludingMember(@Param("memberId") Long memberId, @Param("tag") TagEnum tag);
}

