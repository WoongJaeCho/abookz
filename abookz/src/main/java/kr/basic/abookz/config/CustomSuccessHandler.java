
package kr.basic.abookz.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class CustomSuccessHandler  implements AuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        PrincipalDetails principalDetails = (PrincipalDetails)oAuth2User;
        System.out.println("oAuth2User = " + oAuth2User);
        System.out.println("principalDetails = " + principalDetails);
        MemberEntity member = principalDetails.getMember();
        System.out.println("u = " + member);
        System.out.println("principalId = " + member.getProvider());
        System.out.println("principalId = " + member.getProviderId());
//        requestCache = new HttpSessionRequestCache();
//        SavedRequest savedRequest = requestCache.getRequest(request, response);
//        String targetUrl = savedRequest == null ? "/" : savedRequest.getRedirectUrl();
//        redirectStrategy.sendRedirect(request, response, targetUrl);
        response.sendRedirect("/");
    }

}
