package kr.basic.abookz.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    public BookDTO insertBook(BookDTO bookDTO) {
        BookEntity existingBook = bookrepository.findByISBN13(Long.valueOf(bookDTO.getISBN13()));
        BookDTO bookDTOChange = bookDTO;
        if (existingBook==null) {
            BookEntity book=mapDTOToEntity(bookDTO);
            bookrepository.save(book);
            return bookDTOChange;
        } else {
            System.out.println("해당 조건을 만족하는 책이 이미 존재합니다.");
            return bookDTOChange;
        }
    }
    public List<BookDTO> findAllByDTOId(Long id){
        List<BookEntity> entities =bookrepository.findAllById(id);
        return entities.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO findByDTOISBN13(Long ISBN13){
        BookEntity entities = bookrepository.findByISBN13(ISBN13);
        return mapEntityToDTO(entities);
    }

    BookDTO mapEntityToDTO(BookEntity bookEntity){
        return mapper.map(bookEntity, BookDTO.class);
    }
    BookEntity mapDTOToEntity(BookDTO bookDTO){
        return mapper.map(bookDTO ,BookEntity.class);
    }


}


