package kr.basic.abookz.repository;


import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BookRepository extends JpaRepository<BookEntity, Long> {

       List<BookEntity> findByISBN13(Long ISBN13);

       List<BookEntity> findAllById(Long id);
}
