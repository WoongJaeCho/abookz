package kr.basic.abookz.repository;


import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public interface BookRepository extends JpaRepository<BookEntity, Long> {
       List<BookEntity> findAllByISBN13(Long ISBN13);
       BookEntity findByISBN13(Long ISBN13);

       List<BookEntity> findAllById(Long id);

       Optional<BookEntity> findById(Long id);

       List<BookEntity> findAllByIdOrderByIdDesc(Long Id);

}
