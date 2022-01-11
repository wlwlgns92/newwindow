package ansan.domain.entity.Board;



import ansan.domain.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

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
    @Column(name="b_num")
    private int b_num;
    @Column(name="b_title")
    private String b_title;
    @Column(name="b_content")
    private String b_content;
    @Column(name="b_writer")
    private String b_writer;
    @Column(name="b_view")
    private String b_view;


}
