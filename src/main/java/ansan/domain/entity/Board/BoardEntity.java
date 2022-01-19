package ansan.domain.entity.Board;


import ansan.domain.entity.BaseTimeEntity;
import ansan.domain.entity.Member.MemberEntity;
import ansan.domain.entity.Reply.ReplyEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table ( name = "board")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BoardEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto key
    @Column(name="bnum")
    private int bnum;
    @Column(name="b_title")
    private String b_title;
    @Column(name = "b_contents" , columnDefinition = "LONGTEXT")
    private String b_content;
    @Column(name="b_writer")
    private String b_writer;
    @Column(name="b_view")
    private int b_view;
    @Column(name="b_img")
    private String b_img;

    // 여러개의 댓글을 저장할 리스트
    @OneToMany(mappedBy = "boardEntity")
    private List<ReplyEntity> replyEntity = new ArrayList<>();

    @ManyToOne// 다 : 1 연관 관계 맵핑
    @JoinColumn(name="mnum") // 연결 컬럽 대상
    private MemberEntity memberEntity;
    // 주의 : 댓글 생성쇼ㅣ 연결한 게시물 엔티티를 넣어주세요
}
