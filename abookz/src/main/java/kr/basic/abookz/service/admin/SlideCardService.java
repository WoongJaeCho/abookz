package kr.basic.abookz.service.admin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.basic.abookz.dto.admin.SlideCardDTO;
import kr.basic.abookz.entity.admin.SlideCardEntity;
import kr.basic.abookz.repository.admin.SlideCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlideCardService {

    private final SlideCardRepository adminRepository;
    private final ModelMapper mapper;

    @PersistenceContext
    private EntityManager em;

    public List<SlideCardDTO> findAll(){
        List<SlideCardEntity> cardEntities = adminRepository.findTop3ByOrderByIdx();
        System.out.println("cardEntities = " + cardEntities);
        return cardEntities.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    private SlideCardDTO mapEntityToDTO(SlideCardEntity slideCardEntity){
        return mapper.map(slideCardEntity, SlideCardDTO.class);
    }
}
