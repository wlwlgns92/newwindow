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
    private int b_num;

    @Column
    private String b_title;
    @Column
    private String b_content;
    @Column
    private String b_writer;
    @Column
    private String b_view;


}
