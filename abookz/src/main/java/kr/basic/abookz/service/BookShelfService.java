package kr.basic.abookz.service;

import jakarta.persistence.EntityNotFoundException;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.book.TagEnum;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.repository.BookShelfRepository;
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
public class BookShelfService {

    private final BookShelfRepository bookShelfRepository;
    private final ModelMapper mapper;


     public List<BookShelfDTO> findAllDTOByMemberId(Long memberId) {
        List<BookShelfEntity> entities = bookShelfRepository.findAllByMemberId(memberId);

        return entities.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }
    @Transactional
     public String insertBookShelfCheck(BookShelfDTO bookShelfDTO){
       BookShelfEntity bookShelfEntity = mapDTOToEntity(bookShelfDTO);
   BookShelfEntity checkBookShelf =    bookShelfRepository.findByMemberIdAndBookId(
           bookShelfEntity.getMember().getId(),bookShelfEntity.getBook().getId());
           if(checkBookShelf == null){
               bookShelfRepository.save(bookShelfEntity);
               return "저장";
    }
            return  "실패";
    }
    public List<BookShelfDTO> findAllByMemberIdAndTag(Long memId,TagEnum tagEnum){
        List<BookShelfEntity> entities = bookShelfRepository.findAllByMemberIdAndTag(memId,tagEnum);
        return entities.stream().map(this::mapEntityToDTO).collect(Collectors.toList());
    }
    public String bookShelfUpdate(BookShelfDTO bookShelfDTO){
        BookShelfEntity bookShelf = bookShelfRepository.findById(bookShelfDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("BookShelf not found with id " + bookShelfDTO.getId()));
        BookShelfEntity bookShelfSave = mapDTOToEntity(bookShelfDTO);
        System.out.println("bookShelfSave = " + bookShelfSave);
        bookShelf.setTag(bookShelfSave.getTag());
        bookShelf.setStartDate(bookShelfSave.getStartDate());

        bookShelfRepository.save(bookShelf);
        return "성공";
    }



    BookShelfDTO mapEntityToDTO(BookShelfEntity entity) {
        BookShelfDTO shelfDTO = mapper.map(entity, BookShelfDTO.class);
        BookDTO bookDTO = mapper.map(entity.getBook(), BookDTO.class);
        MemberDTO memberDTO = mapper.map(entity.getMember(), MemberDTO.class);
        shelfDTO.setBookDTO(bookDTO);
        shelfDTO.setMemberDTO(memberDTO);
        return shelfDTO;
    }
    BookShelfEntity mapDTOToEntity(BookShelfDTO shelfDTO) {
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
