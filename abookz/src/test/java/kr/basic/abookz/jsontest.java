package kr.basic.abookz;

import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.service.AladinService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class jsontest {
  @Autowired
  AladinService aladinService;
  RestTemplate restTemplate = new RestTemplate();
  @Test
  void test() throws Exception {
//        String jsonData = "{\"item\":[{\"subInfo\":{\"ratingInfo\":{\"ratingScore\":9.5,\"ratingCount\":15,\"commentReviewCount\":1,\"myReviewCount\":14}}}]}";
    BookDTO vo = null;
    String urlOne = aladinService.UrlGetOneBookItemPage("9788964964859");
    String getOneBookPage  =restTemplate.getForObject(urlOne, String.class);
    System.out.println("urlOne = " + urlOne);
    System.out.println("getOneBookPage = " + getOneBookPage);
//    String getOneBookPage  =restTemplate.getForObject(urlOne, String.class);  // 페이지쪽수 가져오기
//    vo = aladinService.getOneDetail(getOneBookPage);

    JSONObject jsonObject = new JSONObject(getOneBookPage);



    // 'item' 배열에서 첫 번째 아이템을 가져옴
        JSONObject item = jsonObject.getJSONArray("item").getJSONObject(0);

        // 'subInfo' 객체 접근
        JSONObject subInfo = item.getJSONObject("subInfo");

        // 'ratingInfo' 객체 접근
        JSONObject ratingInfo = subInfo.getJSONObject("ratingInfo");
    JSONObject packingInfo = subInfo.getJSONObject("packing");


    // 필요한 정보 추출
        double ratingScore = ratingInfo.getDouble("ratingScore");
        int ratingCount = ratingInfo.getInt("ratingCount");
        int commentReviewCount = ratingInfo.getInt("commentReviewCount");
        int myReviewCount = ratingInfo.getInt("myReviewCount");
        

        System.out.println("Rating Score: " + ratingScore);
        System.out.println("Rating Count: " + ratingCount);
        System.out.println("Comment Review Count: " + commentReviewCount);
        System.out.println("My Review Count: " + myReviewCount);
    System.out.println("packingInfo = " + packingInfo);


  }
}
