package kr.basic.abookz.service.admin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.basic.abookz.dto.admin.SlideCardDTO;
import kr.basic.abookz.entity.admin.SlideCardEntity;
import kr.basic.abookz.repository.admin.SlideCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlideCardService {

    private final SlideCardRepository cardRepository;
    private final ModelMapper mapper;

    @Value("${upload.path}" + "slides/")
    private String uploadPath;

    @PersistenceContext
    private EntityManager em;

    public List<SlideCardDTO> findAllOrderByIdx(){
        List<SlideCardEntity> cardEntities = cardRepository.findAllByOrderByIdx();
        System.out.println("cardEntities = " + cardEntities);
        return cardEntities.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public List<SlideCardDTO> findAll(){
        List<SlideCardEntity> cardEntities = cardRepository.findAll();
        return cardEntities.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public void deletebyId(Long id){
        cardRepository.deleteById(id);
    }

    public void upload(SlideCardDTO slideCardDTO, MultipartFile file){
        try {
            if(file != null && !file.isEmpty()){
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                slideCardDTO.setSlideFileName(fileName);

                Path upload = Paths.get(uploadPath);
                if(!Files.exists(upload)){
                    Files.createDirectories(upload);
                }
                Path filePath = upload.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SlideCardEntity entity = mapDTOToEntity(slideCardDTO);
        entity.setAddDate(LocalDate.now());
        cardRepository.save(entity);
    }
    private SlideCardDTO mapEntityToDTO(SlideCardEntity slideCardEntity){
        return mapper.map(slideCardEntity, SlideCardDTO.class);
    }

    private SlideCardEntity mapDTOToEntity(SlideCardDTO slideCardDTO){
        return mapper.map(slideCardDTO, SlideCardEntity.class);
    }
}
