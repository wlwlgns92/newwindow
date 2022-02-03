package ansan.domain.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum Role { // 멤버 등급 [ 권한 ] ,
    // enum 열거형 자료형  [ 객체도 배열처럼 사용 ]
    // class : 클래스 자료형
    // interface : 인터페이스 자료형

    ADMIN("ROLE_ADMIN","관리자"), // 생성자를 통해 key와 title 할당
    MEMBER("ROLE_MEMBER", "일반회원");

    private String key;
    private String title;

}
