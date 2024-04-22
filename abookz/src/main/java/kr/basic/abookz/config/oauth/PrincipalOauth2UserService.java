
package kr.basic.abookz.config.oauth;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.config.oauth.provider.GoogleUserInfo;
import kr.basic.abookz.config.oauth.provider.KakaoUserInfo;
import kr.basic.abookz.config.oauth.provider.NaverUserInfo;
import kr.basic.abookz.config.oauth.provider.OAuth2UserInfo;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    // userQuest 는 구글에서 코드를 받아서 accessToken을 응답 받는객체
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("user request clientRegistration : " + userRequest.getClientRegistration());
        System.out.println("user reuset getAccessToken :" + userRequest.getAccessToken().getTokenValue());
        OAuth2User oAuth2User = super.loadUser(userRequest); // google 회원 프로필 조회
        System.out.println("oAuth2User = " + oAuth2User);
        System.out.println("get Attribute : " + oAuth2User.getAttributes());

        return processOAuthUser(userRequest , oAuth2User);
    }
    private OAuth2User processOAuthUser(OAuth2UserRequest userRequest , OAuth2User oAuth2User){
        // Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인");
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")){
            System.out.println("카카오 로그인");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            System.out.println("요청 실패 ");
        }
        //System.out.println("oAuth2UserInfo.getProvider() : " + oAuth2UserInfo.getProvider());
        //System.out.println("oAuth2UserInfo.getProviderId() : " + oAuth2UserInfo.getProviderId());
        Optional<MemberEntity> userOptional =
            memberRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

        MemberEntity member;
        if (!userOptional.isPresent()) {
            // user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
            member = MemberEntity.builder()
                .loginId(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                .email(oAuth2UserInfo.getEmail())
                .provider(oAuth2UserInfo.getProvider())
                .providerId(oAuth2UserInfo.getProviderId())
                .name(oAuth2UserInfo.getName())
                .profile(oAuth2UserInfo.getProfile())
                .build();
            memberRepository.save(member);
        }else{
            member = userOptional.get();
        }


        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        session.setAttribute("profileImage", member.getProfile());

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }


}
