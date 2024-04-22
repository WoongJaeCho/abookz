
package kr.basic.abookz.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{
    // attributes ={ id= , email= , name }
    private Map<String, Object> attributes;
    public KakaoUserInfo(Map<String, Object> attributes ){
        this.attributes = attributes;
    }
    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }
    @Override
    public String getProvider() {
        return "kakao";
    }
    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount != null) {
            return (String) kakaoAccount.get("email");
        }
        return null;
    }
    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties != null) {
            return (String) properties.get("nickname");
        }
        return null;
    }

    @Override
    public String getProfile() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties != null) {
            return (String) properties.get("profile_image");
        }
        return null; // 프로필 이미지가 없는 경우를 처리
    }}
