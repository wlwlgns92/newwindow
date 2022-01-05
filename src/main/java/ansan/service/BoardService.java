package ansan.service;


import ansan.domain.dto.BoardDto;
import ansan.domain.entity.Board.BoardEntity;
import ansan.domain.entity.Board.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<BoardEntity> boardEntities = boardRepository.findAll();
        ArrayList<BoardDto> boardDtos = new ArrayList<>();

        for(BoardEntity boardEntity : boardEntities) {
            BoardDto boardDto = new BoardDto(
                    boardEntity.getB_num(),
                    boardEntity.getB_title(),
                    boardEntity.getB_content(),
                    boardEntity.getB_writer(),
                    boardEntity.getCreatedDate(),
                    boardEntity.getB_view()
                    );
            boardDtos.add(boardDto);
        }
        return boardDtos;
    }
}
