package kr.basic.abookz.service;

import kr.basic.abookz.dto.MemoDTO;
import kr.basic.abookz.entity.review.MemoEntity;
import kr.basic.abookz.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    public void save(MemoDTO memoDTO) {
        MemoEntity memoEntity = MemoEntity.toMemoEntity(memoDTO);
        System.out.println("memoEntity = " + memoEntity);
        memoRepository.save(memoEntity);
    }
}
