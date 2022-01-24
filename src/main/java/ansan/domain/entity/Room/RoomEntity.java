package ansan.domain.entity.Room;

import ansan.domain.entity.BaseTimeEntity;
import ansan.domain.entity.Member.MemberEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "roomimgEntities")
public class RoomEntity extends BaseTimeEntity {

    @Id // pk [ 기본키 : 테이블 1개당 기본키 1개 권장 ]
    @GeneratedValue( strategy = GenerationType.IDENTITY ) // 오토키
    @Column( name = "rnum") // 필드 속성 ( name ="필드명" )
    private int rnum;
    // 이름
    @Column( name = "rname")
    private String rname;
    //가격
    @Column( name = "rprice")
    private String rprice;
    //면적
    @Column( name = "rarea")
    private int rarea;
    //관리비
    @Column( name = "rmanagementfee")
    private int rmanagementfee;
    //준공날짜
    @Column( name = "rcompletiondate")
    private String rcompletiondate;
    //입주가능일
    @Column( name = "rindate")
    private String rindate;
    //구조
    @Column( name = "rstructure")
    private String rstructure;
    //층/건물층수
    @Column( name = "rfloor")
    private String rfloor;
    //건물종류
    @Column( name = "rkind")
    private String rkind;
    //주소
    @Column( name = "raddress")
    private String raddress;
    // 내용
    @Column( name = "rcontents")
    private String rcontents;
    //상태
    @Column( name = "ractive")
    private String ractive;
    //거래방식 [ 전세 , 월세 , 매매 ]
    @Column( name = "rtrans")
    private String rtrans;

    //회원번호 관계
    @ManyToOne
    @JoinColumn(name="mnum") // 해당 컬럼의 이름
    private MemberEntity memberEntity;

    // 이미지 관계 room 삭제시 이미도 같이 삭제 [ 제약조건 : cascade = ALL ]
    @OneToMany(mappedBy = "roomEntity", cascade = CascadeType.ALL)
    private List<RoomimgEntity> roomimgEntities = new ArrayList<>();

}
