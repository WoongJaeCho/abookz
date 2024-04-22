package kr.basic.abookz.service;

import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.dto.MemoDTO;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.review.MemoEntity;
import kr.basic.abookz.repository.BookShelfRepository;
import kr.basic.abookz.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final BookShelfRepository shelfRepository;
    private final ModelMapper mapper;
    public void save(MemoDTO memoDTO) {
        MemoEntity memoEntity = MemoEntity.toMemoEntity(memoDTO);
        System.out.println("memoEntity = " + memoEntity);
        memoRepository.save(memoEntity);
    }

    public void delete(Long id) {
        memoRepository.deleteById(id);
    }

    public List<MemoDTO> findAllbyId(Long bookshelfId){
        List<MemoEntity> memoEntities = memoRepository.findAllByBookShelf_id(bookshelfId);
        List<MemoDTO> memoDTOS = new ArrayList<>();
        for (MemoEntity memoEntity : memoEntities){
            memoDTOS.add(MemoDTO.toMemoDTO(memoEntity));
        }
        return memoDTOS;
    }

    public int[] memoCount(Long memId){
        List<BookShelfEntity> bookShelfEntities = shelfRepository.findAllByMemberId(memId);
        int[] memoCount = new int[bookShelfEntities.size()];
        int idx = 0;
        for(BookShelfEntity bookShelf : bookShelfEntities){
            Long bookshelfId = bookShelf.getId();
            int count = memoRepository.countDistinctByBookShelf_Id(bookshelfId);
            System.out.println("count = " + count);
            memoCount[idx++] = count;
        }
        
        return memoCount;
    }

    public List<MemoDTO> memoLastOne(Long memId){
        List<BookShelfEntity> bookShelfEntities = shelfRepository.findAllByMemberId(memId);
        List<MemoDTO> memos = new ArrayList<>();
        int idx=0;
        for(BookShelfEntity bookShelf : bookShelfEntities) {
            Long bookshelfId = bookShelf.getId();
            if(memoRepository.findTopByBookShelf_IdOrderByPageDesc(bookshelfId) != null) {
                MemoEntity memoEntity = memoRepository.findTopByBookShelf_IdOrderByPageDesc(bookshelfId);
                MemoDTO memoDTO = MemoDTO.toMemoDTO(memoEntity);
                memos.add(idx, memoDTO);
            } else {
                memos.add(idx, null);
            }
            idx++;
        }
        System.out.println("memos = " + memos);
        return memos;
    }
    public String getOneMemoUpdate(MemoDTO memoDTO) {

        Optional<MemoEntity> optionalMemoEntity = memoRepository.findById(memoDTO.getId());

        if (!optionalMemoEntity.isPresent()) {
            return "잘못된 접근입니다";
        }


        MemoEntity memoEntity = optionalMemoEntity.get();
        MemoEntity updateEntity = mapDTOtoEntity(memoDTO);
        memoEntity.setNote(updateEntity.getNote());
        memoEntity.setQuotes(updateEntity.getQuotes());
        memoRepository.save(memoEntity);
        return "변경이 완료되었습니다";
    }


    public MemoDTO mapEntityToDTO(MemoEntity memoEntity){
        MemoDTO memoDTO = mapper.map(memoEntity, MemoDTO.class);
        return  memoDTO;
    }
    public MemoEntity mapDTOtoEntity(MemoDTO memoDTO){
        MemoEntity memoEntity =mapper.map(memoDTO, MemoEntity.class);
        return  memoEntity;
    }
}
