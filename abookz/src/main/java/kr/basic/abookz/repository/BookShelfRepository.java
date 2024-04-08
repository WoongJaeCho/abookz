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
import java.util.stream.Collectors;

public interface BookShelfRepository extends JpaRepository<BookShelfEntity,Long> {


    List<BookShelfEntity> findAllByMemberId(Long memberId);
    BookShelfEntity findByMemberIdAndBookId(Long memberId,Long bookId);
    List<BookShelfEntity> findAllByMemberIdAndTag(Long memberId, TagEnum tagEnum);



}

