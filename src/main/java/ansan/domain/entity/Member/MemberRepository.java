package ansan.domain.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

    // 필드검색 finby 필드명

    // 엔티티 검색 finby 필드명
    Optional<MemberEntity> findBymid(String mid);

    // 해당 필드로 엔티티 검색 [ 생성해야함 ] findBym필드명(자료형 필드명);

}
