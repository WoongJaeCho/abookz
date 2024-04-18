package kr.basic.abookz.service;

import kr.basic.abookz.dto.BookDTO;

import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.CategoryEnum;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AladinService {

    private final RestTemplate restTemplate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public AladinService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    private static final String TTB_KEY = "ttbjun_40201143003"; // 여기에 TTBKey를 입력합니다.

    /* 상품 여러개 가져오는 json 타입형*/
    public Page<BookDTO> searchItems(String searchWord, Pageable pageable) throws Exception {
        String url = getUrl(searchWord);
        String response = restTemplate.getForObject(url, String.class);
        List<BookDTO> allBooks = parseItemsFromJson(response);

        // 페이징 처리
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allBooks.size());
        List<BookDTO> books = new ArrayList<>();

        if (start <= end) {
            books = allBooks.subList(start, end);
        }

        return new PageImpl<>(books, pageable, allBooks.size());
    }

    /*상품 1개 가져오기 */
    public BookDTO searchGetOneItem(String isbn13) throws  Exception{
            BookDTO vo = null;
            String urlOne = UrlGetOneBookItemPage(isbn13);
            String getOneBookPage  =restTemplate.getForObject(urlOne, String.class);  // 페이지쪽수 가져오기
            vo = getOneDetail(getOneBookPage);
        return  vo;
    }
    public  Page<BookDTO> choiceGetCategoryList(String category ,Pageable pageable) throws Exception{
        List<BookDTO>  vo = null;
        String url = getUrlCategoryList(category);
        String choice = restTemplate.getForObject(url,String.class);
        vo=parseItemsFromJson(choice);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), vo.size());
        List<BookDTO> books = new ArrayList<>();

        if (start <= end) {
            books = vo.subList(start, end);
        }
        Page<BookDTO> page = new PageImpl<>(books, pageable, vo.size());
        return page;
    }
    public Page<BookDTO> getQueryPagingList(String type, Pageable pageable) throws  Exception{
        List<BookDTO> vo = null;
        String url = getUrlQueryTypeList(type);
        String query = restTemplate.getForObject(url,String.class);
        vo=parseItemsFromJson(query);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), vo.size());
        List<BookDTO> books = new ArrayList<>();

        if (start <= end) {
            books = vo.subList(start, end);
        }
        Page<BookDTO> page = new PageImpl<>(books, pageable, vo.size());
        return  page;
    }
    public List<BookDTO> getQueryTypeList(String type) throws  Exception{
        List<BookDTO> vo = null;
        String url = getUrlQueryTypeList(type);
        String query = restTemplate.getForObject(url,String.class);
        vo=parseItemsFromJson(query);
        return vo;
    }
    public BookDTO getOneBookDTO(String isbn13) throws Exception{
        BookDTO entity = null;
        String urlOne = UrlGetOneBookItemPage(isbn13);
        String getOneBookPage = restTemplate.getForObject(urlOne, String.class);
        entity = getOneDTO(getOneBookPage);
    return entity;
    }


    /*상품 검색 조회*/
    private String getUrl(String searchWord) throws Exception {
        return  "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey="+ TTB_KEY +"&Query="+ searchWord
                +"&QueryType=Title&Cover=big&MaxResults=100&start=1&SearchTarget=Book&output=js&Version=20131101";

    }

    public String UrlGetOneBookItemPage(String isbn13) throws  Exception{
        return  "http://www.aladin.co.kr/ttb/api/ItemLookup.aspx?ttbkey="+ TTB_KEY +"&ItemIdType=ISBN&ItemId="+ isbn13
                +"&Cover=big&MaxResults=100&start=1&SearchTarget=Book&output=js&Version=20131101&OptResult=packing,ratingInfo";

    }
    private String getUrlCategoryList(String category) throws  Exception{
        return  "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey="+ TTB_KEY+"&QueryType=BestSeller" +"&CategoryId=" +category
                +"&Cover=big&MaxResults=100&start=1&SearchTarget=Book&output=js&Version=20131101";
    }
    private String getUrlQueryTypeList(String type) throws  Exception{
        return "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey="+ TTB_KEY+"&QueryType="+type
                +"&Cover=big&MaxResults=100&start=1&SearchTarget=Book&output=js&Version=20131101";
    };

    private List<BookDTO> parseItemsFromJson(String jsonString) {
        List<BookDTO> items = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("item");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject itemObject = jsonArray.getJSONObject(i);
            String title = itemObject.getString("title");
            String author = itemObject.getString("author");
            String publisher = itemObject.getString("publisher");
            String change = itemObject.getString("pubDate");
            LocalDate pubDate = LocalDate.parse(change, formatter);
           String categoryName = itemObject.getString("categoryName");
            int index = categoryName.indexOf(">");
            String[] categories = categoryName.split(">");
            CategoryEnum cate = null;
            String selectedCategory ="";
            if(categories.length==0 || categories.length == 1 ) {
                for(CategoryEnum category : CategoryEnum.values()){
                    if(category.getCategoryName().equals(selectedCategory)){
                    cate=category;
                        }
                }

            }else {
                 selectedCategory = categories[1].trim().toLowerCase();
                System.out.println("selectedCategory = " + selectedCategory);
                for (CategoryEnum category : CategoryEnum.values()) {
                    if (category.getCategoryName().equals(selectedCategory)) {
                        cate = category;

                    }
                }
            }
            String cover = itemObject.getString("cover");
            String description = itemObject.getString("description");

            String link = itemObject.getString("link");
            if(itemObject.has("isbn13") || !itemObject.getString("isbn13").isEmpty()) {
                String isbn = itemObject.getString("isbn");
                String isbn13 = itemObject.getString("isbn13");
                BookDTO bookDTO = BookDTO.builder()
                        .title(title)
                        .author(author)
                    /*    .categoryName(cate)*/
                        .publisher(publisher)
                        .pubDate(pubDate)
                        .ISBN(isbn13)
                        .ISBN(isbn)
                        .cover(cover)
                        .description(description)
                        .link(link)
                        // .itemPage(itemPage) // 페이지 수를 처리하는 로직을 추가해야 한다면 이 부분을 활성화합니다.
                        .build();
                items.add(bookDTO);
            }

        }
        return items;
    }

    public BookDTO getOneDetail(String jsonIsbn13){

        JSONObject jsonObject = new JSONObject(jsonIsbn13);
        if(jsonObject.length() < 3){
            return null;
        }
        JSONArray jsonArray = jsonObject.getJSONArray("item");
        JSONObject itemObject = jsonArray.getJSONObject(0);
        JSONObject subInfoObject = itemObject.getJSONObject("subInfo");
        JSONObject ratingInfo = subInfoObject.getJSONObject("ratingInfo");
        System.out.println("ratingInfo = " + ratingInfo);
        Long aladinGrade = ratingInfo.getLong("ratingScore");
        System.out.println("aladinGrade = " + aladinGrade);
        JSONObject packingInfo = subInfoObject.getJSONObject("packing");
        int weight = packingInfo.getInt("weight");
        int sizeDepth = packingInfo.getInt("sizeDepth");
        String title = itemObject.getString("title");
        String author = itemObject.getString("author");
        String publisher = itemObject.getString("publisher");
        String change = itemObject.getString("pubDate");
        LocalDate pubDate = LocalDate.parse(change, formatter);
        /*CategoryEntity categoryName= CategoryEntity.fromString(itemObject.getString("categoryName"));*/
        String cover = itemObject.getString("cover");
        String description = itemObject.getString("description");
        //int itemPage = itemObject.getInt("")
        String link = itemObject.getString("link");
        int itemPage  = subInfoObject.getInt("itemPage");
        String isbn13 = itemObject.getString("isbn13");
        String isbn = itemObject.getString("isbn");
        BookDTO item = null;

             item = BookDTO.builder()
                    .title(title)
                    .author(author)
                    .publisher(publisher)
                    .pubDate(pubDate)
                    .ISBN(isbn)
                    .ISBN13(isbn13)
                    .cover(cover)
                    .description(description)
                    .aladinGrade(aladinGrade)
                    .link(link)
                    .itemPage(itemPage)
                    .weight(weight)
                    .sizeDepth(sizeDepth)
                    .build();


        return item;
        }
    private BookDTO getOneDTO(String jsonIsbn13){
        JSONObject jsonObject = new JSONObject(jsonIsbn13);
        JSONArray jsonArray = jsonObject.getJSONArray("item");
        JSONObject itemObject = jsonArray.getJSONObject(0);
        JSONObject subInfoObject = itemObject.getJSONObject("subInfo");
        JSONObject ratingInfo = subInfoObject.getJSONObject("ratingInfo");
        JSONObject packingInfo = subInfoObject.getJSONObject("packing");
        int weight = packingInfo.getInt("weight");
        int sizeDepth = packingInfo.getInt("sizeDepth");
        String title = itemObject.getString("title");
        String author = itemObject.getString("author");
        String publisher = itemObject.getString("publisher");
        String change = itemObject.getString("pubDate");
        Long aladinGrade = ratingInfo.getLong("ratingScore");
        LocalDate pubDate = LocalDate.parse(change, formatter);
        /*CategoryEntity categoryName= CategoryEntity.fromString(itemObject.getString("categoryName"));*/
        String cover = itemObject.getString("cover");
        String description = itemObject.getString("description");
        //int itemPage = itemObject.getInt("")
        String link = itemObject.getString("link");
        int itemPage  = subInfoObject.getInt("itemPage");
        String isbnString = null;
        String isbn13String = null;
        String isbn13 = itemObject.getString("isbn13");

        BookDTO item = BookDTO.builder()
                .title(title)
                .author(author)
                .publisher(publisher)
                .pubDate(pubDate)
                .ISBN13(isbn13)
                .cover(cover)
                .description(description)
                .link(link)
                .aladinGrade(aladinGrade)
                .itemPage(itemPage)
                .weight(weight)
                .sizeDepth(sizeDepth)
                .build();
        System.out.println("item = " + item);

        return item;
    }




}


