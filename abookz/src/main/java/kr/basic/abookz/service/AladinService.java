package kr.basic.abookz.service;

import kr.basic.abookz.entity.BookEntity;
import kr.basic.abookz.entity.CategoryEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AladinService {

    private final RestTemplate restTemplate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public AladinService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    private static final String TTB_KEY = "ttbjun_40201143003"; // 여기에 TTBKey를 입력합니다.

    public List<BookEntity> searchItems(String searchWord) throws Exception {
        String url = getUrl(searchWord);
        String response = restTemplate.getForObject(url, String.class);

        return parseItemsFromJson(response);
    }

    private String getUrl(String searchWord) throws Exception {
        return  "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey="+ TTB_KEY +"&Query="+ searchWord
                +"&QueryType=Title&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101";

    }

    private List<BookEntity> parseItemsFromJson(String jsonString) {
        List<BookEntity> items = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println(jsonString);
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
                String isbnString = itemObject.getString("isbn");
                Long ISBN  = Long.valueOf(isbnString);
                items.add(new BookEntity(title,author,publisher,ISBN,pubDate/*categoryName*/,cover,description,link));
            }else{
                String isbn13String = itemObject.getString("isbn13");
                Long ISBN13 = Long.valueOf(isbn13String);

                items.add(new BookEntity(title,author,publisher,pubDate,ISBN13/*categoryName*/,cover,description,link));
            }


        }
        return items;
    }
}


