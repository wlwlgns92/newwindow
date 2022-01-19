package ansan.domain.entity.Board;

import ansan.domain.entity.Reply.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer> {

}
