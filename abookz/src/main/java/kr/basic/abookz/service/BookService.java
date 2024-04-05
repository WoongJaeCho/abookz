package kr.basic.abookz.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.TagEnum;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.repository.BookRepository;
import kr.basic.abookz.repository.BookShelfRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookrepository;
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insertBook(BookEntity bookEntity) {
        List<BookEntity> existingBooks = bookrepository.findByISBN13(bookEntity.getISBN13());

        // 해당 조건을 만족하는 엔티티가 존재하지 않으면 새로운 책 추가
        if (existingBooks.isEmpty()) {
            bookrepository.save(bookEntity);
        } else {
            System.out.println("해당 조건을 만족하는 책이 이미 존재합니다.");
        }
    }
    public List<BookEntity> findAllById(Long id){
        return bookrepository.findAllById(id);
    }


}


