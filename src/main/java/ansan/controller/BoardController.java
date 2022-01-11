package ansan.controller;


import ansan.domain.dto.BoardDto;
import ansan.domain.dto.MemberDto;
import ansan.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class BoardController {

    // 메모리 할당
    @Autowired
    BoardService boardService;

    @GetMapping("/board/boardlist")
    public String boardlist(Model model) {
        ArrayList<BoardDto> boardDtos = boardService.boardlist();
        model.addAttribute("BoardDtos", boardDtos);
        return "board/boardlist";  
    } // 타임리프를 이용한 html 반환
    
    // 게시물 작성페이지 이동
    @GetMapping("/board/boardwrite")
    public String boardwrite() { return "board/boardwrite"; }

    // boardview 페이지 이동
    @GetMapping("/board/boardview/{b_num}") //Get 방식으로 URL 맵핑 // @PathVariable("호출한 변수명") 타입 변수명 : 경로에 있는 변수를 호출
    public String boardview(@PathVariable("b_num") int b_num , Model model) {
        BoardDto boardDto = boardService.getboard(b_num);

        model.addAttribute("boardDto", boardDto);
        return "board/boardview"; // 타임리프를 이용한 html 호출
    }
    
    @Autowired
    HttpServletRequest request;

    @PostMapping("/board/boardwritecontroller")
    public String boardwritecontroller(BoardDto boardDto) {
        // 세션 선언
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto)session.getAttribute("logindto"); // 세션 호출
        boardDto.setB_writer(memberDto.getM_id());
        System.out.println(boardDto);
        boardService.boardwrite(boardDto);
        return "redirect:/board/boardlist"; // 작성 성공시 list로 이동
    }

}
