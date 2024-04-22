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
import kr.basic.abookz.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.basic.abookz.entity.book.TagEnum.READ;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookShelfService {

  private final BookShelfRepository bookShelfRepository;
  private final ModelMapper mapper;
  private final BookRepository bookRepository;
  private final MemberRepository memberRepository;
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
      bookShelfRepository.save(bookShelfEntity);
      return "저장";
    }
    return "실패";

  }

  public List<BookShelfDTO> findAllByMemberIdAndTag(Long memId, TagEnum tagEnum) {
    List<BookShelfEntity> entities = bookShelfRepository.findAllByMemberIdAndTag(memId, tagEnum);
    return entities.stream().map(this::mapEntityToDTO).collect(Collectors.toList());
  }

  public List<BookShelfDTO> findAllByMemberIdAndTagOrderByIdDesc(Long memId, TagEnum tagEnum) {
    List<BookShelfEntity> entities = bookShelfRepository.findAllByMemberIdAndTagOrderByIdDesc(memId, tagEnum);
    return entities.stream().map(this::mapEntityToDTO).collect(Collectors.toList());
  }

  public String bookShelfUpdateTag(BookShelfDTO bookShelfDTO) {
    LocalDateTime now = LocalDateTime.now();
    BookShelfEntity bookShelf = bookShelfRepository.findById(bookShelfDTO.getId())
        .orElseThrow(() -> new EntityNotFoundException("BookShelf not found with id " + bookShelfDTO.getId()));
    System.out.println("bookShelf = " + bookShelf);
    BookShelfEntity bookShelfSave = mapDTOToEntity(bookShelfDTO);
    System.out.println("bookShelfSave = " + bookShelfSave);
    BookShelfEntity.BookShelfEntityBuilder builder = BookShelfEntity.builder();
    TagEnum tagEnum = bookShelfDTO.getTag();
    System.out.println("tagEnum = " + tagEnum);

    if (tagEnum == READ) {
      if (bookShelf.getStartDate() == null) {
        bookShelf.setStartDate(now);
      }
      bookShelf.setEndDate(now);
      bookShelf.setTag(tagEnum);
      bookShelf.setBookShelfGrade(bookShelfSave.getBookShelfGrade());
      bookShelf.setCurrentPage(bookShelf.getBook().getItemPage());
      return "성공";
    } else if (tagEnum == TagEnum.CURRENTLY_READING) {
      bookShelfSave = builder.startDate(now).build();
      bookShelf.setStartDate(bookShelfSave.getStartDate());
      bookShelf.setTag(tagEnum);
      bookShelf.setBookShelfGrade(bookShelfSave.getBookShelfGrade());
      return "성공";
    }

    bookShelf.setTag(tagEnum);
    bookShelf.setEndDate(null);
    bookShelf.setStartDate(null);
    bookShelf.setCurrentPage(0);
    return "성공";

  }

  public String bookShelfUpdateDate(BookShelfDTO bookShelfDTO) {
    BookShelfEntity bookShelf = bookShelfRepository.findById(bookShelfDTO.getId())
        .orElseThrow(() -> new EntityNotFoundException("BookShelf not found with id " + bookShelfDTO.getId()));
    BookShelfEntity bookShelfSave = mapDTOToEntity(bookShelfDTO);
    bookShelf.setStartDate(bookShelfSave.getStartDate());
    bookShelf.setTargetDate(bookShelfSave.getTargetDate());
    bookShelf.setCurrentPage(bookShelfSave.getCurrentPage());
    bookShelf.setTargetDate(bookShelfSave.getTargetDate());
    LocalDateTime now = LocalDateTime.now();

    int itemPage = bookShelf.getBook().getItemPage();
    if (bookShelfSave.getCurrentPage() == itemPage) {
      if (bookShelf.getStartDate() == null) {
        bookShelf.setStartDate(now);
      }
      bookShelf.setTag(READ);
      bookShelf.setEndDate(now);
      return "성공";
    }
    bookShelf.setEndDate(bookShelfSave.getEndDate());
    bookShelf.setTag(TagEnum.CURRENTLY_READING);
    bookShelf.setStartDate(now);
    return "성공";
  }

  public Slice<BookShelfDTO> SliceBookShelfDTO(Long id, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Slice<BookShelfEntity> bookShelfSlice = bookShelfRepository.findAllByMemberIdOrderByIdDesc(id, pageable);
    return bookShelfSlice.map(this::mapEntityToDTO);
  }

  public Slice<BookShelfDTO> SliceBookShelfDTOIdAndTag(Long id, TagEnum tagEnum, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Slice<BookShelfEntity> bookShelfSlice = bookShelfRepository.findAllByMemberIdAndTagOrderByIdDesc(id, tagEnum, pageable);
    return bookShelfSlice.map(this::mapEntityToDTO);
  }

  public String deleteBookShelf(Long id, Long memberId) {
    BookShelfEntity bookShelfEntity = bookShelfRepository.findByIdAndMemberId(id, memberId);
    if (bookShelfEntity == null) {
      System.out.println("bookShelfEntity = " + bookShelfEntity);
      return "fail";
    }
    bookShelfRepository.deleteById(bookShelfEntity.getId());
    return "suc";
  }

  public BookShelfDTO findByBookShelfId(Long id) {
    BookShelfEntity bookShelf = bookShelfRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("BookShelf not found with id " + id));
    BookShelfDTO bookShelfDTO = mapEntityToDTO(bookShelf);
    return bookShelfDTO;
  }


  public void updateGrade(BookShelfDTO shelfDTO) {
    BookShelfEntity bookShelfEntity = bookShelfRepository.findById(shelfDTO.getId())
        .orElseThrow(() -> new EntityNotFoundException("BookShelf not found with id " + shelfDTO.getId()));

    bookShelfEntity.setBookShelfGrade(shelfDTO.getBookShelfGrade());
    bookShelfRepository.save(bookShelfEntity);
  }

  public BookShelfDTO findByIdAndBookId(Long id, Long bookId) {
    Optional<BookShelfEntity> findBookShelf = bookShelfRepository.findByIdAndBookId(id, bookId);
    BookShelfEntity bookShelf = findBookShelf.get();

    return mapEntityToDTO(bookShelf);
  }

  public BookShelfDTO findById(Long bookShelfId) {
    Optional<BookShelfEntity> findBookShelf = bookShelfRepository.findById(bookShelfId);
    BookShelfEntity bookShelf = findBookShelf.get();

    return mapEntityToDTO(bookShelf);
  }

  public BookDTO mapBookEntitytoBookDTO(BookEntity bookEntity) {
    BookDTO bookDTO = mapper.map(bookEntity, BookDTO.class);
    return bookDTO;
  }

  public BookEntity mapBookDTOtoEntity(BookDTO bookDTO) {
    BookEntity bookEntity = mapper.map(bookDTO, BookEntity.class);
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
    if (shelfDTO.getBookDTO() != null) {
      BookEntity bookEntity = mapper.map(shelfDTO.getBookDTO(), BookEntity.class);
      shelfEntity.setBook(bookEntity);
    }
    if (shelfDTO.getMemberDTO() != null) {
      MemberEntity memberEntity = mapper.map(shelfDTO.getMemberDTO(), MemberEntity.class);
      shelfEntity.setMember(memberEntity);
    }
    return shelfEntity;
  }
  public double averageBooksOfRead(){
    List<Object[]> results = bookShelfRepository.findTotalBooksByMemberForReadBooks();
//    int memberCnt = memberRepository.countbyId();
    double totalBooks = 0;
    for (Object[] result : results) {
      Long memberId = (Long) result[0];
      Long sum = (Long) result[1];  // DB에서 가져온 값은 Long일 수 있습니다.

      double books = sum != null ? sum.doubleValue() : 0.0;
      totalBooks += books;
    }
    System.out.println(totalBooks);
    System.out.println(results.size());

    return results.size() > 0 ? totalBooks / results.size() : 0.0;
  }
  public double averageHeightOfReadBooks(){
    List<Object[]> results = bookShelfRepository.findTotalHeightByMemberForReadBooks();
    double totalHeight = 0;
    for (Object[] result : results) {
      Long memberId = (Long) result[0];
      Long sumHeight = (Long) result[1];  // DB에서 가져온 값은 Long일 수 있습니다.

      double Height = sumHeight != null ? sumHeight.doubleValue() : 0.0;
      totalHeight += Height;
    }

    return results.size() > 0 ? totalHeight / results.size() : 0.0;
  }

  public double averageWeightOfReadBooks() {
    List<Object[]> results = bookShelfRepository.findTotalWeightByMemberForReadBooks();
    double totalWeight = 0;
    for (Object[] result : results) {
      Long memberId = (Long) result[0];
      Long sumWeight = (Long) result[1];  // DB에서 가져온 값은 Long일 수 있습니다.

      // Long 타입의 sumWeight를 Double로 변환
      double weight = sumWeight != null ? sumWeight.doubleValue() : 0.0;
      totalWeight += weight;
    }

    return results.size() > 0 ? totalWeight / results.size() : 0.0;
  }

  public BookShelfDTO findByMember_IdAndBook_ISBN13(Long id, String isbn13)  throws RuntimeException{
    return bookShelfRepository.findByMember_IdAndBook_ISBN13(id, Long.valueOf(isbn13))
        .map(this::mapEntityToDTO)
        .orElseThrow(() -> new RuntimeException("책꽂이나 ISBN에 해당하는 데이터가 없습니다."));
  }

  public double averageWeightOfReadBooksExcludingCurrent(Long currentUserId) {
    return bookShelfRepository.findAll().stream()
        .filter(shelf -> !shelf.getMember().getId().equals(currentUserId) && shelf.getTag().equals(READ))
        .mapToDouble(shelf -> shelf.getBook().getWeight())
        .average()
        .orElse(0.0); // 만약 해당되는 데이터가 없을 경우 0.0 반환
  }

}
