package kr.basic.abookz.service;

import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.repository.BookShelfRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookShelfService {

    private final BookShelfRepository bookShelfRepository;
    private final ModelMapper mapper;

    public BookShelfEntity save(BookShelfEntity bookShelfEntity){
        return bookShelfRepository.save(bookShelfEntity);
    }


     public List<BookShelfDTO> findAllDTOByMemberId(Long memberId) {
        List<BookShelfEntity> entities = bookShelfRepository.findAllByMemberId(memberId);
         System.out.println("entities = " + entities);

        return entities.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }




    BookShelfDTO mapEntityToDTO(BookShelfEntity entity) {
        BookShelfDTO shelfDTO = mapper.map(entity, BookShelfDTO.class);
        System.out.println("shelfDTO = " + shelfDTO);
        BookDTO bookDTO = mapper.map(entity.getBook(), BookDTO.class);
        System.out.println("bookDTO = " + bookDTO);
        MemberDTO memberDTO = mapper.map(entity.getMember(), MemberDTO.class);
        System.out.println("memberDTO = " + memberDTO);
        shelfDTO.setBookDTO(bookDTO);
        shelfDTO.setMemberDTO(memberDTO);
        return shelfDTO;
    }

}
