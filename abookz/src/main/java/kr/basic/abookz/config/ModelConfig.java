
package kr.basic.abookz.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {
    // Model Mapper
    // DTO < = > Entity 타입 형변환하기위해 만들엇음

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
