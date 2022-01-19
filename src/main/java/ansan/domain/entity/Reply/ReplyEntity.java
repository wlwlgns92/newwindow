package ansan.domain.entity.Reply;

import ansan.domain.entity.BaseTimeEntity;
import ansan.domain.entity.Board.BoardEntity;
import ansan.domain.entity.Member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="reply")
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyEntity extends BaseTimeEntity {
    // BoardEntity와 연결  [ 책 p.198 ]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rnum")
    private int rnum;

    @Column(name="rcontent")
    private String rcontent;

    @Column(name="rwriter")
    private String rwriter;


    @ManyToOne// 다 : 1 연관 관계 맵핑
    @JoinColumn(name="bnum") // 연결 컬럽 대상
    private BoardEntity boardEntity;
     // 주의 : 댓글 생성쇼ㅣ 연결한 게시물 엔티티를 넣어주세요

    @ManyToOne// 다 : 1 연관 관계 맵핑
    @JoinColumn(name="mnum") // 연결 컬럽 대상
    private MemberEntity memberEntity;
}
