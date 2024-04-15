package kr.basic.abookz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//페이징DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookPagingDTO {
    private String query;
    private int page;
    private int size;
    private String sort;
}
