package kr.basic.abookz.service;

import kr.basic.abookz.dto.MemoDTO;
import kr.basic.abookz.entity.review.MemoEntity;
import kr.basic.abookz.repository.MemoRepository;
import kr.basic.abookz.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public void save(MemoDTO memoDTO) {
        MemoEntity memoEntity = MemoEntity.toMemoEntity(memoDTO);
        System.out.println("memoEntity = " + memoEntity);
        memoRepository.save(memoEntity);
    }
}
