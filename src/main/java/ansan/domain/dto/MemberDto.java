package ansan.domain.dto;


import ansan.domain.entity.Member.MemberEntity;
import ansan.domain.entity.Member.Role;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor // 깡통생성자
@AllArgsConstructor // 풀 생성자
@Getter // Get 메소드
@Setter // Set 메소드
@ToString // 객체주소정보 - > 객체내 필드
@Builder
public class MemberDto {
    // 필드
    private int m_num; // 회원번호
    private String m_id;  // 회원아이디
    private String m_password; // 비밀번호
    private String m_name; // 이름
    private String m_sex; // 성별
    private String m_phone; // 연락처
    private String m_email; // 이메일
    private String m_address; // 주소
    private int m_point; // 포인트
    private Role m_grade; // 등급
    private LocalDateTime m_createdDate;

   // 회원가입시 : DTO -> Entity 변환  [ 빌더로 대체 가능 ]
    public MemberEntity toentity() {
        return MemberEntity.builder()
        .mid(this.m_id)
        .m_password(this.m_password)
                .m_name(this.m_name)
                .m_sex(this.m_sex)
                .m_phone(this.m_phone)
                .memail(this.m_email)
                .m_point(this.m_point)
                .m_address(this.m_address)
                .m_grade(Role.MEMBER) // 회원가입시 기본으로 MEMBER 등급 권한 부여
                .build();


    }

}
