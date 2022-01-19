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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rnum")
    private int rnum;
    // 방이름
    @Column(name="rname")
    private String rname;
    // 가격
    @Column(name="rprice")
    private int rprice;

    @Column(name="rarea")
    private int rarea;

    @Column(name="rmanagementfee")
    private int rmanagementfee;

    @Column(name="rcompletiondate")
    private String rcompletiondate;

    @Column(name="rstructure")
    private String rstructure;

    @Column(name="rfloor")
    private String rfloor;
    //건물종륲
    @Column(name="rkind")
    private String rkind;

    @Column(name="raddress")
    private String raddress;

    @Column(name="rcontents")
    private String rcontents;

    @Column(name="ractive")
    private String ractive;

    @Column(name="rtrans")
    private String rtrans;

    //회원번호 관계
    @ManyToOne
    @JoinColumn(name="mnum") // 해당 컬럼의 이름
    private MemberEntity memberEntity;

    // 이미지 관계
    @OneToMany(mappedBy = "roomEntity")
    private List<RoomimgEntity> roomimgEntities = new ArrayList<>();

}
