package kr.basic.abookz.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.repository.BookRepository;
import kr.basic.abookz.repository.BookShelfRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookrepository;
    private final ModelMapper mapper;
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public List<BookEntity> insertBook(BookEntity bookEntity) {
        List<BookEntity> existingBooks = bookrepository.findByISBN13(bookEntity.getISBN13());


        if (existingBooks.isEmpty()) {
            bookrepository.save(bookEntity);
            return existingBooks;
        } else {
            System.out.println("해당 조건을 만족하는 책이 이미 존재합니다.");
        }
        return existingBooks;
    }
    public List<BookDTO> findAllByDTOId(Long id){
        List<BookEntity> entities =bookrepository.findAllById(id);
        return entities.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }
    public List<BookDTO> findByDTOISBN13(Long ISBN13){
        List<BookEntity> entities = bookrepository.findByISBN13(ISBN13);
        return entities.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    BookDTO mapEntityToDTO(BookEntity bookEntity){
        return mapper.map(bookEntity, BookDTO.class);
    }





}


