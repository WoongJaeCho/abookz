package kr.basic.abookz.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.book.TagEnum;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.repository.BookRepository;
import kr.basic.abookz.repository.BookShelfRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookShelfService {

    private final BookShelfRepository bookShelfRepository;
    private final ModelMapper mapper;
    private final BookRepository bookRepository;
    @PersistenceContext
    private final EntityManager entityManager;

     public List<BookShelfDTO> findAllDTOByMemberId(Long memberId) {
        List<BookShelfEntity> entities = bookShelfRepository.findAllByMemberId(memberId);

        return entities.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }
    public List<BookShelfDTO> findAllDTOByMemberIdOrderByIdDesc(Long memberId) {
        List<BookShelfEntity> entities = bookShelfRepository.findAllByMemberIdOrderByIdDesc(memberId);

        return entities.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public String insertBookAndBookShelf(BookDTO bookDTO, BookShelfDTO bookShelfDTO) {

        BookEntity saveBook = mapBookDTOtoEntity(bookDTO);

        BookEntity existingBook = bookRepository.findByISBN13(saveBook.getISBN13());

        if (existingBook == null) {
            entityManager.persist(saveBook);
            entityManager.flush();
        } else {
            saveBook = existingBook;

        }

        BookShelfEntity bookShelfEntity = mapDTOToEntity(bookShelfDTO);
        bookShelfEntity.setBook(saveBook);
        bookShelfEntity.setBookShelfGrade(0.0);
        bookShelfEntity.setTag(TagEnum.WANT_TO_READ);
        BookShelfEntity existingBookShelf = bookShelfRepository.findByMemberIdAndBookId(
                bookShelfEntity.getMember().getId(), bookShelfEntity.getBook().getId());

        if (existingBookShelf == null) {
            // 책꽂이가 존재하지 않으면 새로 저장
            bookShelfRepository.save(bookShelfEntity);
            return "저장";
        } else {
            // 이미 책꽂이에 존재하면 실패 반환
            return "실패";
        }
    }
    public List<BookShelfDTO> findAllByMemberIdAndTag(Long memId,TagEnum tagEnum){
        List<BookShelfEntity> entities = bookShelfRepository.findAllByMemberIdAndTag(memId,tagEnum);
        return entities.stream().map(this::mapEntityToDTO).collect(Collectors.toList());
    }
    public List<BookShelfDTO> findAllByMemberIdAndTagOrderByIdDesc(Long memId,TagEnum tagEnum){
        List<BookShelfEntity> entities = bookShelfRepository.findAllByMemberIdAndTagOrderByIdDesc(memId,tagEnum);
        return entities.stream().map(this::mapEntityToDTO).collect(Collectors.toList());
    }
    public String bookShelfUpdateTag(BookShelfDTO bookShelfDTO){
        LocalDateTime now = LocalDateTime.now();
        BookShelfEntity bookShelf = bookShelfRepository.findById(bookShelfDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("BookShelf not found with id " + bookShelfDTO.getId()));
        System.out.println("bookShelf = " + bookShelf);
        BookShelfEntity bookShelfSave = mapDTOToEntity(bookShelfDTO);
        System.out.println("bookShelfSave = " + bookShelfSave);
        BookShelfEntity.BookShelfEntityBuilder builder = BookShelfEntity.builder();
        TagEnum tagEnum =bookShelfDTO.getTag();
        System.out.println("tagEnum = " + tagEnum);
            if(tagEnum == TagEnum.READ) {
                System.out.println("값체크 Read");
                bookShelfSave = builder.endDate(now).build();
                bookShelf.setEndDate(bookShelfSave.getEndDate());
                bookShelf.setTag(tagEnum);
                bookShelf.setBookShelfGrade(bookShelfSave.getBookShelfGrade());

                return "성공";
            }else if(tagEnum == TagEnum.CURRENTLY_READING) {
                System.out.println("값체크 커런트");
                bookShelfSave = builder.startDate(now).build();
                bookShelf.setStartDate(bookShelfSave.getStartDate());
                bookShelf.setTag(tagEnum);
                bookShelf.setBookShelfGrade(bookShelfSave.getBookShelfGrade());
                return "성공";
            }

                bookShelf.setTag(tagEnum);
                    bookShelf.setEndDate(null);
              bookShelf.setStartDate(null);
            return "성공";

    }
    public String bookShelfUpdateDate(BookShelfDTO bookShelfDTO){
        BookShelfEntity bookShelf = bookShelfRepository.findById(bookShelfDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("BookShelf not found with id " + bookShelfDTO.getId()));
        System.out.println("bookShelf = " + bookShelf);
        BookShelfEntity bookShelfSave = mapDTOToEntity(bookShelfDTO);
        System.out.println("bookShelfSave = " + bookShelfSave);
        bookShelf.setStartDate(bookShelfSave.getStartDate());
        bookShelf.setTargetDate(bookShelfSave.getTargetDate());
        bookShelf.setEndDate(bookShelfSave.getEndDate());
        bookShelf.setCurrentPage(bookShelfSave.getCurrentPage());
        bookShelf.setTargetDate(bookShelfSave.getTargetDate());
        return"성공";
    }

    public String deleteBookShelf(Long Id,Long memberId){
      BookShelfEntity bookShelfEntity = bookShelfRepository.findByIdAndMemberId(Id,memberId);
      if(bookShelfEntity == null){
          System.out.println("bookShelfEntity = " + bookShelfEntity);
          return "fail";
      }
        System.out.println(" 성공" );
        bookShelfRepository.deleteById(bookShelfEntity.getId());
      return "suc";
    }

    public BookShelfDTO findByBookShelfId(Long id){
        BookShelfEntity bookShelf = bookShelfRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BookShelf not found with id " + id));
        BookShelfDTO bookShelfDTO = mapEntityToDTO(bookShelf);
        return  bookShelfDTO;
    }





    public void updateGrade(BookShelfDTO shelfDTO) {
        BookShelfEntity bookShelfEntity = bookShelfRepository.findById(shelfDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("BookShelf not found with id " + shelfDTO.getId()));

        bookShelfEntity.setBookShelfGrade(shelfDTO.getBookShelfGrade());
        bookShelfRepository.save(bookShelfEntity);
    }

    public BookShelfDTO findByIdAndBookId(Long id,Long bookId){
      Optional<BookShelfEntity> findBookShelf = bookShelfRepository.findByIdAndBookId(id, bookId);
      BookShelfEntity bookShelf = findBookShelf.get();

    return mapEntityToDTO(bookShelf);
    }

  public BookShelfDTO findById(Long bookShelfId) {
    Optional<BookShelfEntity> findBookShelf = bookShelfRepository.findById(bookShelfId);
    BookShelfEntity bookShelf = findBookShelf.get();

    return mapEntityToDTO(bookShelf);
  }
  public BookDTO mapBookEntitytoBookDTO(BookEntity bookEntity){
     BookDTO bookDTO =mapper.map(bookEntity, BookDTO.class);
     return bookDTO;
  }
  public BookEntity mapBookDTOtoEntity(BookDTO bookDTO){
        BookEntity bookEntity =mapper.map(bookDTO, BookEntity.class);
        return bookEntity;
    }

    public BookShelfDTO mapEntityToDTO(BookShelfEntity entity) {
        BookShelfDTO shelfDTO = mapper.map(entity, BookShelfDTO.class);
        BookDTO bookDTO = mapper.map(entity.getBook(), BookDTO.class);
        MemberDTO memberDTO = mapper.map(entity.getMember(), MemberDTO.class);
        shelfDTO.setBookDTO(bookDTO);
        shelfDTO.setMemberDTO(memberDTO);
        return shelfDTO;
    }

    public BookShelfEntity mapDTOToEntity(BookShelfDTO shelfDTO) {
        BookShelfEntity shelfEntity = mapper.map(shelfDTO, BookShelfEntity.class);
        if(shelfDTO.getBookDTO()!= null){
            BookEntity bookEntity = mapper.map(shelfDTO.getBookDTO(), BookEntity.class);
            shelfEntity.setBook(bookEntity);
        }
        if(shelfDTO.getMemberDTO()!= null) {
            MemberEntity memberEntity = mapper.map(shelfDTO.getMemberDTO(), MemberEntity.class);
            shelfEntity.setMember(memberEntity);
        }
        return shelfEntity;
    }
}
