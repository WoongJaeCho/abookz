package kr.basic.abookz.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookrepository;
    private final ModelMapper mapper;
/*    @PersistenceContext
    private EntityManagerFactory entityManagerFactory;*/
    @Transactional
    public BookDTO insertBook(BookDTO bookDTO){

            BookEntity  saveBook=mapDTOToEntity(bookDTO);
           BookEntity bookEntity =bookrepository.findByISBN13(saveBook.getISBN13());
           if(bookEntity== null){
               /* EntityManager entityManager=entityManagerFactory.createEntityManager();
                entityManager.getTransaction().begin();*/
                BookEntity insert = bookrepository.save(saveBook);
               return bookDTO;
           }
        System.out.println("중복 ISBN값 존재");
           return bookDTO;
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


  public BookDTO findById(Long bookId) {
    Optional<BookEntity> findBook = bookrepository.findById(bookId);
    return mapEntityToDTO(findBook.get());
  }
}


