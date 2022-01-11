package ansan.service;


import ansan.domain.dto.BoardDto;
import ansan.domain.entity.Board.BoardEntity;
import ansan.domain.entity.Board.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

    // 게시물 리스트
    public ArrayList<BoardDto> boardlist() {
        
        // 게시물 번호로 정렬해서 엔티티 호출
        // SQL : Select * from board order by 필드명 DESC
        // JPA : findAll( Sort.by(Sort.Direction.DESC, "Entity 필드명") ) ;
        List<BoardEntity> boardEntities = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
        ArrayList<BoardDto> boardDtos = new ArrayList<>();
        for(BoardEntity boardEntity : boardEntities) {

            // 날짜 형변환 [ localdate -> String ]
                // LocalDateTime.format() : LocalDateTime
            String date = boardEntity.getCreatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd"));
            // 오늘날짜
            String nowdate = LocalDateTime.now().format( DateTimeFormatter.ofPattern("yy-MM-dd") );
            // 만약에 게시물 작성일이 오늘이면 시간을 출력 아니면 날짜를 출력
            if(date.equals(nowdate)){
                // 오늘날짜
                date = boardEntity.getCreatedDate().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
            }
            BoardDto boardDto = new BoardDto(
                    boardEntity.getB_num(),
                    boardEntity.getB_title(),
                    boardEntity.getB_content(),
                    boardEntity.getB_writer(),
                    date,
                    boardEntity.getB_view()
                    );
            boardDtos.add(boardDto);
        }
        return boardDtos;
    }

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
                .build();
    }
}
