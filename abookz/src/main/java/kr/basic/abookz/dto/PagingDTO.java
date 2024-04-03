package kr.basic.abookz.dto;

import lombok.Data;


//페이징DTO
@Data
public class PagingDTO {
    private int page;
    private int size;
    private String sort;
    private String desc;
}
