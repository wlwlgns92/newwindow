package ansan.domain.dto;


import ansan.domain.entity.Member.MemberEntity;
import ansan.domain.entity.Member.Role;
import lombok.*;

import java.util.Map;

@NoArgsConstructor // 깡통생성자
@AllArgsConstructor // 풀 생성자
@Getter // Get 메소드
@Setter // Set 메소드
@ToString // 객체주소정보 - > 객체내 필드
@Builder
public class Oauth2Dto {

    // 이름
    String name;
    // 이메일
    String email;
    // 회원정보
    private Map<String, Object> attribute;
    // 요청 토큰키
    private String nameattributekey;

    // 클라이언트 구분용 [ 카카오 or 네이버 or 구글 ]
    public static Oauth2Dto of(String registrationid, String nameattributekey, Map<String, Object> atrribute) {
        if(registrationid.equals("kakao")) { return ofkakao( nameattributekey, atrribute); }
        else if(registrationid.equals("naver")) { return ofnaver( nameattributekey, atrribute); }
        else {return null;} //구글
    }
    // 카카오정보 dto 변환 메소드
    private static Oauth2Dto ofkakao(String nameattributekey, Map<String, Object> atrribute) {
        Map<String, Object> kakao_account = (Map<String, Object>) atrribute.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");

        return Oauth2Dto.builder()
                .name((String) profile.get("nickname"))
                .email((String) kakao_account.get("email"))
                .attribute(atrribute)
                .nameattributekey(nameattributekey)
                .build();
    }

    // 네이버 dto 변환 메소드
    private static Oauth2Dto ofnaver(String nameattributekey, Map<String, Object> atrribute) {

        Map<String, Object> response = (Map<String, Object>) atrribute.get("response");

        return Oauth2Dto.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .attribute(atrribute)
                .nameattributekey(nameattributekey)
                .build();
    }

    // DTo - > Entity 변환
    // 첫 로그인했을때 DTO -> Entity [ 회원가입 ]
    public MemberEntity toentity() {
        return MemberEntity.builder()
                .m_name(this.name)
                .memail(this.email)
                .m_grade(Role.MEMBER)
                .build();
    }
}

