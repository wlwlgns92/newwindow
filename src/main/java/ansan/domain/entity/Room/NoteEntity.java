package ansan.domain.entity.Room;

import ansan.domain.entity.BaseTimeEntity;
import ansan.domain.entity.Member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="note")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NoteEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int nnum; // 문의번호

    @Column(name="ncontent") // 문의 내용
    private String ncontent;

    @Column(name="nreply") // 답변
    private String nreply;

    @Column(name="nread")
    private int nread; // 0이면 읽지않음, 1이면 읽음

    @ManyToOne
    @JoinColumn( name = "mnum")
    private MemberEntity memberEntity; // 보낸사람

    @ManyToOne
    @JoinColumn( name = "rnum")
    private RoomEntity roomEntity; // 문의 방 or 받는사람
}
