package ansan.domain.dto;


import ansan.domain.entity.Board.BoardEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BoardDto {

    private int b_num;
    private String b_title;
    private String b_content;
    private String b_writer;
    private String b_createdDate;
    private String b_view;
    private String b_img;

    // Dto - > Entity 변환
    public BoardEntity toentity() {
        return BoardEntity.builder()
                .b_title(this.b_title)
                .b_content(this.b_content)
                .b_writer(this.b_writer)
                .b_img(this.b_img)
                .build();

    }
}

