package ansan.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ReplyDto {

    private int rnum;
    private String rcontent;
    private String rwriter;
    private LocalDateTime rcreatedDate;

}
