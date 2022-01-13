package ansan.controller;


import ansan.domain.dto.BoardDto;
import ansan.domain.dto.MemberDto;
import ansan.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class BoardController {

    // 메모리 할당
    @Autowired
    BoardService boardService;

    // 게시물 리스트
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
    // 게시물 수정페이지 이동
    @GetMapping("/board/boardupdate/{b_num}")
    public String boardupdate(@PathVariable("b_num") int b_num, Model model) {
        BoardDto boardDto = boardService.getboard(b_num);
        model.addAttribute("boardDto", boardDto);
        return "board/boardupdate";
    }

    @Autowired
    HttpServletRequest request;

    // 게시물작성
    @PostMapping("/board/boardwritecontroller")
    public String boardwritecontroller(@RequestParam("b_img") MultipartFile file) throws IOException {
        //파일업로드 [ 첨부파일 ] JSP ( COS 라이브러리 ) -> Spring (MultipartFile 클래스)
        String dir = "C:\\Users\\505\\Desktop\\newwindow\\src\\main\\resources\\static\\upload";
        String filepath = dir + "\\" + file.getOriginalFilename(); // 저장 경로 + form에서 첨부한 파일 이름 호출
        // file.getOriginalFilename(); form에 첨부된 파일 호출
        file.transferTo(new File(filepath));
        // transferTo() : 파일저장 [ 스트림없이 ] , 예외처리 발생

        // 제목 내용 호출
        String b_title = request.getParameter("b_title");
        String b_content = request.getParameter("b_content");
        // 세션 선언
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto)session.getAttribute("logindto"); // 세션 호출

        BoardDto boardDto = BoardDto.builder()
                .b_title(request.getParameter("b_title"))
                .b_content(request.getParameter("b_content"))
                .b_writer(memberDto.getM_id())
                .b_img(file.getOriginalFilename())
                .build();
        boardDto.setB_title(b_title);
        boardDto.setB_content(b_content);
        boardDto.setB_img(file.getOriginalFilename());



        boardDto.setB_writer(memberDto.getM_id());
        System.out.println(boardDto);
        boardService.boardwrite(boardDto);
        return "redirect:/board/boardlist"; // 작성 성공시 list로 이동
    }
    //게시물 삭제 처리
    @GetMapping("/board/bdelete")
    @ResponseBody
    public int bdelete(@RequestParam("b_num") int b_num) {
        boolean result = boardService.bdelete(b_num);
        if(result) {
            return 1;
        }else {
            return 2;
        }
    }

    // 게시물 수정 처리
    @PostMapping("/board/boardcontroller")
    public String boardupdatecontroller(BoardDto boardDto){
        boolean result = boardService.boardupdate(boardDto);
        if(result) {
            return "board/boardview";
        }
        else {
            return "board/boardview";
        }
    }


}
