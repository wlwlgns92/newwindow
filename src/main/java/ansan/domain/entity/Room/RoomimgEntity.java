package ansan.domain.entity.Room;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder // 객체 생성시 안정성 보장 [ new 생성자() -> Builder ]
@Setter
@ToString(exclude = "roomEntity") // Object [ 객체의 주소값 ] : ToString -> [ 모든 필드의 내용물 ]
@Getter
@Table(name = "roomimg")
public class RoomimgEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rimgnum")
    private int rimgnum;

    @Column(name = "rimg")
    private String rimg;

    @ManyToOne
    @JoinColumn(name="rnum")
    private RoomEntity roomEntity;
}
