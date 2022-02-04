package ansan.service;

import ansan.domain.dto.MemberDto;
import ansan.domain.dto.Oauth2Dto;
import ansan.domain.entity.Member.MemberEntity;
import ansan.domain.entity.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;

@Service
public class OauthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override // 소셜 로그인 후 회원정보 가져오기 메소드, 꼭 구현을 해야함
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 회원정보 속성 가져오기
        String nameattributekey = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 클라이언트 정보
        String registrationid = userRequest.getClientRegistration().getRegistrationId(); // 클라이언트 아이디 가져오기

        // DTO
        Oauth2Dto oauth2Dto = Oauth2Dto.of(registrationid,nameattributekey, oAuth2User.getAttributes() );
        // DB저장
        MemberEntity memberEntity = saveorupdate(oauth2Dto);
        // 세션 할당

        MemberDto loginDto = MemberDto.builder()
                .m_id(memberEntity.getMid())
                .m_num(memberEntity.getM_num())
                .build();
        HttpSession session = request.getSession(); // 서버내 세션 가져오기
        session.setAttribute("logindto", loginDto);

        // 리턴
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(memberEntity.getRoleKey())),
                oauth2Dto.getAttribute(),
                oauth2Dto.getNameattributekey()
        );
    }
    @Autowired
    HttpServletRequest request;

    @Autowired
    private MemberRepository memberRepository;

    // 동일한 이메일이 있을경우 업데이트 동일한 이메일이 없으면 저장
    public MemberEntity saveorupdate( Oauth2Dto oauth2Dto){

        // 1. memberRepository를 이용해 동일한 이메일 찾기 [ 'findBy필드명' 의 반환타입은 Optional ]
        MemberEntity memberEntity = memberRepository.findBymemail(oauth2Dto.getEmail())
                .map(entity -> entity.update(oauth2Dto.getName())) // Map
                .orElse(oauth2Dto.toentity()); // orElse() 동일한 이메일이 없을경우 dto -> entity

        // Optional 클래스
            // 1. orElse(해당 Optional 객체가 null 이면) : 여러개 있을 경우 모두처리
            // 2. map(해당 값 -> 이벤트)


        return memberRepository.save( memberEntity );
    }
}
