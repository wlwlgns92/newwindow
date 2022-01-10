package ansan.controller;


import ansan.domain.dto.BoardDto;
import ansan.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class BoardController {
    @GetMapping("/board/boardlist")
    public String boardlist(Model model) {

        ArrayList<BoardDto> boardDtos = boardService.boardlist();
        model.addAttribute("BoardDtos", boardDtos);
        return "board/boardlist";  } // 타임리프를 이용한 html 반환

    @GetMapping("/board/boardwrite")
    public String boardwrite() { return "board/boardwrite"; }

    @Autowired
    BoardService boardService;

    @PostMapping("/board/boardwritecontroller")
    public String boardwritecontroller(BoardDto boardDto) {
        boardService.boardwrite(boardDto);
        return "redirect:board/boardlist"; // 작성 성공시 list로 이동
    }

}
