package kr.basic.abookz.service;

import kr.basic.abookz.dto.BookDTO;

import kr.basic.abookz.entity.book.BookEntity;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
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
    public List<BookDTO> searchItems(String searchWord) throws Exception {
        String url = getUrl(searchWord);
        String response = restTemplate.getForObject(url, String.class);

        return parseItemsFromJson(response);
    }
    /*상품 1개 가져오기 */
    public BookDTO searchGetOneItem(String isbn13) throws  Exception{
            BookDTO vo = null;
            String urlOne = UrlGetOneBookItemPage(isbn13);
            String getOneBookPage  =restTemplate.getForObject(urlOne, String.class);  // 페이지쪽수 가져오기
            vo = getOneDetail(getOneBookPage);
        return  vo;
    }
    public List<BookDTO> choiceGetCategoryList(String category) throws Exception{
        List<BookDTO>  vo = null;
        String url = getUrlCategoryList(category);
        String choice = restTemplate.getForObject(url,String.class);
                vo=parseItemsFromJson(choice);
        return vo;
    }
    public List<BookDTO> getQueryTypeList(String type) throws  Exception{
        List<BookDTO> vo = null;
        String url = getUrlQueryTypeList(type);
        String query = restTemplate.getForObject(url,String.class);
        vo=parseItemsFromJson(query);
        return vo;
    }
    public BookEntity getOneBookEntity(String isbn13) throws Exception{
        BookEntity entity = null;
        String urlOne = UrlGetOneBookItemPage(isbn13);
        String getOneBookPage = restTemplate.getForObject(urlOne, String.class);
        entity = getOneEntity(getOneBookPage);
    return entity;
    }


    /*상품 검색 조회*/
    private String getUrl(String searchWord) throws Exception {
        return  "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey="+ TTB_KEY +"&Query="+ searchWord
                +"&QueryType=Title&Cover=big&MaxResults=100&start=1&SearchTarget=Book&output=js&Version=20131101";

    }

    private String UrlGetOneBookItemPage(String isbn13) throws  Exception{
        return  "http://www.aladin.co.kr/ttb/api/ItemLookup.aspx?ttbkey="+ TTB_KEY +"&ItemIdType=ISBN&ItemId="+ isbn13
                +"&Cover=big&MaxResults=100&start=1&SearchTarget=Book&output=js&Version=20131101";

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
            /*CategoryEntity categoryName= CategoryEntity.fromString(itemObject.getString("categoryName"));*/
            String cover = itemObject.getString("cover");
            String description = itemObject.getString("description");

            //int itemPage = itemObject.getInt("")
            String link = itemObject.getString("link");

            if(itemObject.getString("isbn13") == null) {
                String isbn = itemObject.getString("isbn");

                BookDTO bookDTO = BookDTO.builder()
                        .title(title)
                        .author(author)
                        .publisher(publisher)
                        .pubDate(pubDate)
                        .ISBN(isbn) // ISBN이 null일 수 있으므로 빌더에서 처리해야 합니다.
                        /*.ISBN13(ISBN13)
                        .categoryName(categoryName) // 문자열로 카테고리 이름을 처리합니다.*/
                        .cover(cover)
                        .description(description)
                        .link(link)
                        // .itemPage(itemPage) // 페이지 수를 처리하는 로직을 추가해야 한다면 이 부분을 활성화합니다.
                        .build();
                items.add(bookDTO);
            }else {
                String isbn13 = itemObject.getString("isbn13");
                BookDTO bookDTO = BookDTO.builder()
                        .title(title)
                        .author(author)
                        .publisher(publisher)
                        .pubDate(pubDate)
                        /*.ISBN(ISBN) // ISBN이 null일 수 있으므로 빌더에서 처리해야 합니다.*/
                        .ISBN13(isbn13)
                        /*.categoryName(categoryName) // 문자열로 카테고리 이름을 처리합니다.*/
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

    private BookDTO getOneDetail(String jsonIsbn13){

        JSONObject jsonObject = new JSONObject(jsonIsbn13);
        System.out.println("jsonObject = " + jsonObject);
        JSONArray jsonArray = jsonObject.getJSONArray("item");
        JSONObject itemObject = jsonArray.getJSONObject(0);
        JSONObject subInfoObject = itemObject.getJSONObject("subInfo");
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
        String isbn = null;
        String isbn13 = null;
        if (itemObject.has("isbn13")) {
            isbn13 = itemObject.getString("isbn13");
        } else if (itemObject.has("isbn")) {
    isbn = itemObject.getString("isbn");

        }
        BookDTO item = BookDTO.builder().title(title)
                    .author(author)
                .publisher(publisher)
                .pubDate(pubDate)
                .ISBN(isbn)
                .ISBN13(isbn13)
                .cover(cover)
                .description(description)
                .link(link)
                .itemPage(itemPage)
                .build();


        return item;
        }
    private BookEntity getOneEntity(String jsonIsbn13){
        JSONObject jsonObject = new JSONObject(jsonIsbn13);
        System.out.println("jsonObject = " + jsonObject);
        JSONArray jsonArray = jsonObject.getJSONArray("item");
        JSONObject itemObject = jsonArray.getJSONObject(0);
        JSONObject subInfoObject = itemObject.getJSONObject("subInfo");
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
        String isbnString = null;
        String isbn13String = null;
        Long isbn = null ;
        Long isbn13 = null;
        if (itemObject.has("isbn13")) {
            isbn13String = itemObject.getString("isbn13");
            isbn13 =Long.valueOf(isbn13String);
        } else if (itemObject.has("isbn")) {
            isbnString = itemObject.getString("isbn");
            isbn=Long.valueOf(isbn);
        }
        BookEntity item = BookEntity.builder().title(title)
                .author(author)
                .publisher(publisher)
                .pubDate(pubDate)
                .ISBN(isbn)
                .ISBN13(isbn13)
                .cover(cover)
                .description(description)
                .link(link)
                .itemPage(itemPage)
                .build();


        return item;
    }




}


