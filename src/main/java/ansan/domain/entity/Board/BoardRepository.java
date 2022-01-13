package ansan.domain.entity.Board;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {

    // Jpa 메소드 만들기
    @Query(nativeQuery = true, value="select * from board where b_title like %:search%")
    Page<BoardEntity> findAlltitle(@Param("search") String search, Pageable pageable);
    @Query(nativeQuery = true, value="select * from board where b_content like %:search%")
    Page<BoardEntity> findAllcontent(@Param("search") String search, Pageable pageable);
    @Query(nativeQuery = true, value="select * from board where b_writer like %:search%")
    Page<BoardEntity> findAllwriter(@Param("search") String search, Pageable pageable);

}
