package kr.basic.abookz.service;

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

    @Transactional
    public String save(BookShelfDTO bookShelfDTO){
        BookShelfEntity bookShelfEntity =  mapDTOToEntity(bookShelfDTO);
        BookShelfEntity checkBook= bookShelfRepository.findByMemberIdAndBookId(bookShelfEntity
                .getMember().getId(),bookShelfEntity.getBook().getId());
        BookShelfDTO   dtoCheck = null;
        if(checkBook == null) {
            dtoCheck =mapEntityToDTO(bookShelfEntity);
            bookShelfRepository.save(bookShelfEntity);
            return "";
        }
            System.out.println("해당 조건을 만족하는 책이 이미 존재합니다.");
        dtoCheck =mapEntityToDTO(bookShelfEntity);
             return "책값존재";
    }

     public List<BookShelfDTO> findAllDTOByMemberId(Long memberId) {
        List<BookShelfEntity> entities = bookShelfRepository.findAllByMemberId(memberId);

        return entities.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }
    public List<BookShelfDTO> findAllByMemberIdAndTag(Long memId,TagEnum tagEnum){
        List<BookShelfEntity> entities = bookShelfRepository.findAllByMemberIdAndTag(memId,tagEnum);
        return entities.stream().map(this::mapEntityToDTO).collect(Collectors.toList());
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
        BookEntity bookEntity = mapper.map(shelfDTO.getBookDTO(), BookEntity.class);
        MemberEntity memberEntity= mapper.map(shelfDTO.getMemberDTO(), MemberEntity.class);
        shelfEntity.setBook(bookEntity);
        shelfEntity.setMember(memberEntity);
        return shelfEntity;
    }

}
