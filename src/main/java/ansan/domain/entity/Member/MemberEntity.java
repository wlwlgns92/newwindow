package ansan.domain.entity.Member;


import ansan.domain.entity.BaseTimeEntity;
import ansan.domain.entity.Board.BoardEntity;
import ansan.domain.entity.Reply.ReplyEntity;
import ansan.domain.entity.Room.NoteEntity;
import ansan.domain.entity.Room.RoomEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity // DB내 테이블과 연결
@Table( name = "member") // 테이블 속성
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto key
    private int m_num; // 회원번호

    @Column
    private String mid;  // 회원아이디

    @Column
    private String m_password; // 비밀번호

    @Column
    private String m_name; // 이름

    @Column
    private String m_sex; // 성별

    @Column
    private String m_phone; // 연락처

    @Column
    private String m_email; // 이메일

    @Column
    private String m_address; // 주소

    @Column
    private int m_point; // 포인트

    @Enumerated(EnumType.STRING) // DB는 Role이라는 자료형이 없어서 Enum 타입을 Jpa가 String 값으로 인식가능하도록 String 자료형으로 변환
    @Column
    private Role m_grade; // 등급 만들어놓은 Role(enum) 설정

    //해당 Role의 key를 반환하는 메소드
    public String getRoleKey() { return this.m_grade.getKey(); }

    @OneToMany(mappedBy = "memberEntity")
    private List<BoardEntity> boardEntities = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity")
    private List<ReplyEntity> replyEntities = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity")
    private List<RoomEntity> roomEntities = new ArrayList<>();

    //문의 리스트
    @OneToMany(mappedBy = "memberEntity")
    private List<NoteEntity> noteEntities = new ArrayList<>();
    
}
