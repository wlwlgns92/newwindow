import ansan.domain.entity.Room.RoomEntity;
import ansan.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@TestPropertySource(locations="classpath:application.test.properties")
public class TestController {

    @Autowired
    RoomService roomService;

    @Test
    @DisplayName("방 전체 조회")
    public void boardlist() {

        List<RoomEntity> roomEntities = roomService.getroomlist();
    }
}
