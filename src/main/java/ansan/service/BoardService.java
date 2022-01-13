package ansan.service;


import ansan.domain.dto.BoardDto;
import ansan.domain.entity.Board.BoardEntity;
import ansan.domain.entity.Board.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;
    // 게시물 작성
    public boolean boardwrite(BoardDto boardDto) {
        boardRepository.save(boardDto.toentity()); // save(entity) : insert , update
        return true;
    }
    // 게시물 리스트 [ 페이징 처리 ]
    public Page<BoardEntity> boardlist (Pageable pageable, String keyword, String search) {
        // pageable = PageRequest.of(1,10); // 2번째 페이지 게시물 10개 출력

        //만약에 검색이 있을경우
        if(keyword != null && keyword.equals("b_title")) return boardRepository.findAlltitle(search, pageable);
        if(keyword != null && keyword.equals("b_content")) return boardRepository.findAllcontent(search, pageable);
        if(keyword != null && keyword.equals("b_writer")) return boardRepository.findAllwriter(search, pageable);

        int page = 0;
        if(pageable.getPageNumber() == 0) {
            page = 0;
        }else {
            page = pageable.getPageNumber()-1;
        }
        pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdDate")); // 변수 페이지 10개 출력

        return boardRepository.findAll(pageable);
    }
    // 게시물 리스트
//    public ArrayList<BoardDto> boardlist() {
//
//        // 게시물 번호로 정렬해서 엔티티 호출
//        // SQL : Select * from board order by 필드명 DESC
//        // JPA : findAll( Sort.by(Sort.Direction.DESC, "Entity 필드명") ) ;
//        List<BoardEntity> boardEntities = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
//        ArrayList<BoardDto> boardDtos = new ArrayList<>();
//        for(BoardEntity boardEntity : boardEntities) {
//
//            // 날짜 형변환 [ localdate -> String ]
//                // LocalDateTime.format() : LocalDateTime
//            String date = boardEntity.getCreatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd"));
//            // 오늘날짜
//            String nowdate = LocalDateTime.now().format( DateTimeFormatter.ofPattern("yy-MM-dd") );
//            // 만약에 게시물 작성일이 오늘이면 시간을 출력 아니면 날짜를 출력
//            if(date.equals(nowdate)){
//                // 오늘날짜
//                date = boardEntity.getCreatedDate().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
//            }
//            BoardDto boardDto = new BoardDto(
//                    boardEntity.getB_num(),
//                    boardEntity.getB_title(),
//                    boardEntity.getB_content(),
//                    boardEntity.getB_writer(),
//                    date,
//                    boardEntity.getB_view(),
//                    boardEntity.getB_img(),
//                    null
//                    );
//            boardDtos.add(boardDto);
//        }
//        return boardDtos;
//    }

    // boardview 출력
    public BoardDto getboard(int b_num) {

        Optional<BoardEntity> entityOptional = boardRepository.findById(b_num);

        String date = entityOptional.get().getCreatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd"));

        return BoardDto.builder()
                .b_createdDate(date)
                .b_num(entityOptional.get().getB_num())
                .b_title(entityOptional.get().getB_title())
                .b_content(entityOptional.get().getB_content())
                .b_writer(entityOptional.get().getB_writer())
                .b_view(entityOptional.get().getB_view())
                .b_img(entityOptional.get().getB_img())
                .build();
    }

    // 게시물삭제
    public boolean bdelete(int b_num) {
        Optional<BoardEntity> boardEntity = boardRepository.findById(b_num);
        if(boardEntity.get() != null) {
            boardRepository.delete(boardEntity.get());
            return true;
        }else {
            return false;
        }
    }

    // 게시물 수정
    @Transactional // 수정중 오류 발생시 rollback : 취소
    public boolean boardupdate( BoardDto boardDto ) {
        try {
            // 수정할 엔티티 찾기
            Optional<BoardEntity> entityOptional = boardRepository.findById(boardDto.getB_num());
            // 엔티티 수정
            entityOptional.get().setB_title(boardDto.getB_title());
            entityOptional.get().setB_content(boardDto.getB_content());
            return true;
        }catch (Exception e) {
            System.out.println("수정실패 : " + e);
            return false;
        }
    }
    
}
