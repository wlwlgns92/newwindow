package ansan.controller;


import ansan.domain.dto.BoardDto;
import ansan.domain.dto.MemberDto;
import ansan.domain.entity.Board.BoardEntity;
import ansan.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.UUID;

@Controller
public class BoardController {

    // 메모리 할당
    @Autowired
    BoardService boardService;

    @Autowired
    HttpServletResponse response; // 응답 객체

    @Autowired
    HttpServletRequest request; // 요청객체

    // 게시물 리스트
    @GetMapping("/board/boardlist")
    public String boardlist(Model model, @PageableDefault Pageable pageable) {
        // 검색 서비스
        String keyword = request.getParameter("keyword");
        String search = request.getParameter("search");
        HttpSession session = request.getSession();

        if (keyword != null || search != null) {
            session.setAttribute("keyword", keyword);
            session.setAttribute("search", search);
        } else {
            keyword = (String) session.getAttribute("keyword");
            search = (String) session.getAttribute("search");
        }


        // 페이징처리 서비스
        Page<BoardEntity> boardDtos = boardService.boardlist(pageable, keyword, search);

        model.addAttribute("BoardDtos", boardDtos);

        return "board/boardlist";
    } // 타임리프를 이용한 html 반환

    // 게시물 작성페이지 이동
    @GetMapping("/board/boardwrite")
    public String boardwrite() {
        return "board/boardwrite";
    }

    //첨부파일 다운로드 처리
    @GetMapping("/board/filedownload{b_img}")
    public void filedownload(@RequestParam("b_img") String b_img, HttpServletResponse response) {

        String path = "C:\\Users\\505\\newwindow\\src\\main\\resources\\static\\upload" + b_img;
        //객체화
        File file = new File(path);
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(b_img.split("_")[1], "UTF-8") );
            //파일 객체 내보내기
            OutputStream outputStream = response.getOutputStream();
            //내보내기할 대상 읽어오기 [ 파일 읽기 ]
            FileInputStream fileInputStream = new FileInputStream(path); // 객체로 읽어와도 됨

            int read = 0;
            byte[] buffer = new byte[1024 * 1024]; // 읽어온 바이트를 저장할 배열
            while ((read = fileInputStream.read(buffer)) != -1) { // 파일 읽기 [ .read ]
                outputStream.write(buffer, 0, read); // 파일쓰기 [ .write ]
            }
        } catch (Exception e) {
            System.out.println("다운로드 에러 " + e);
        }
    }

    // boardview 페이지 이동
    @GetMapping("/board/boardview/{b_num}") //Get 방식으로 URL 맵핑 // @PathVariable("호출한 변수명") 타입 변수명 : 경로에 있는 변수를 호출
    public String boardview(@PathVariable("b_num") int b_num, Model model) {
        BoardDto boardDto = boardService.getboard(b_num);
        // 첨부파일 존재하면 uuid가 제거된 파일명 변환해서 b_realimg 담기
        if (boardDto.getB_img() != null) {
            boardDto.setB_realimg(boardDto.getB_img().split("_")[1]);
        }
        model.addAttribute("boardDto", boardDto);
        return "board/boardview"; // 타임리프를 이용한 html 호출
    }

    // 게시물 수정페이지 이동
    @GetMapping("/board/boardupdate/{b_num}")
    public String boardupdate(@PathVariable("b_num") int b_num, Model model) {
        BoardDto boardDto = boardService.getboard(b_num);
        // 첨부파일 존재하면 uuid가 제거된 파일명 변환해서 b_realimg 담기
        if (boardDto.getB_img() != null) {
            boardDto.setB_realimg(boardDto.getB_img().split("_")[1]);
        }
        model.addAttribute("boardDto", boardDto);
        return "board/boardupdate";
    }


    // 게시물작성
    @PostMapping("/board/boardwritecontroller")
    @ResponseBody
    public int boardwritecontroller(@RequestParam("b_img") MultipartFile file) {

        try {
            String uuidfile = null;
            if (!file.getOriginalFilename().equals("")) { // 첨부파일이 있을때
//        String folderpath = request.getSession().getServletContext().getRealPath("/");
//        System.out.println(folderpath);

                // 파일 이름 중복 배제 [ UUID : 범용 고유 식별자 ]
                UUID uuid = UUID.randomUUID(); // 고유식별자 객체 난수 생성 메소드 호출
                // 사용자가 올린 파일명에 _ 존재하면 - 변경
                String OriginalFilename = file.getOriginalFilename();
                uuidfile = uuid.toString() + "_" + OriginalFilename.replaceAll("_", "-");

                //파일업로드 [ 첨부파일 ] JSP ( COS 라이브러리 ) -> Spring (MultipartFile 클래스)
                String dir = "C:\\Users\\505\\newwindow\\src\\main\\resources\\static\\upload";
                String filepath = dir + "\\" + uuidfile; // 저장 경로 + form에서 첨부한 파일 이름 호출
                // file.getOriginalFilename(); form에 첨부된 파일 호출
                file.transferTo(new File(filepath));
                // transferTo() : 파일저장 [ 스트림없이 ] , 예외처리 발생
            } else { // 첨부파일이 없을때
                uuidfile = null;
            }
            // 세션 선언
            HttpSession session = request.getSession();
            MemberDto memberDto = (MemberDto) session.getAttribute("logindto"); // 세션 호출

            BoardDto boardDto = BoardDto.builder()
                    .b_title(request.getParameter("b_title"))
                    .b_content(request.getParameter("b_content"))
                    .b_writer(memberDto.getM_id())
                    .b_img(uuidfile)
                    .build();

            boolean result = boardService.boardwrite(boardDto);

            if (result) {
                //return "redirect:/board/boardlist"; // 작성 성공시 list로 이동
                return 1;
            } else {
                return 2;
            }
        } catch (Exception e) {
            return 2;
        }
    }

    //게시물 삭제 처리
    @GetMapping("/board/bdelete")
    @ResponseBody
    public int bdelete(@RequestParam("b_num") int b_num) {
        boolean result = boardService.bdelete(b_num);
        if (result) {
            return 1;
        } else {
            return 2;
        }
    }

    // 게시물 수정 처리
    @PostMapping("/board/boardcontroller")
    public String boardupdatecontroller(@RequestParam("b_newimg") MultipartFile file,
                                        @RequestParam("b_num") int b_num,
                                        @RequestParam("b_title") String b_title,
                                        @RequestParam("b_content") String b_content,
                                        @RequestParam("b_img") String b_img,
                                        BoardDto boardDto) {

        if (!file.getOriginalFilename().equals("")) { // 변경된 첨부파일이 있으면
            try {
                UUID uuid = UUID.randomUUID();
                String uuidfile = uuid.toString() + "_" + file.getOriginalFilename().replaceAll("_", "-");
                String dir = "C:\\Users\\505\\newwindow\\src\\main\\resources\\static\\upload";
                String filepath = dir + "\\" + uuidfile;
                file.transferTo(new File(filepath));
                boardService.boardupdate(
                        BoardDto.builder().b_num(b_num).b_title(b_title).b_content(b_content).b_img(uuidfile).build());
            } catch (Exception e) {
                System.out.println(e);
            }

        } else { // 변경된 파일이 없으면 -> 기존파일 그대로 사용
            boardService.boardupdate(
                    BoardDto.builder().b_num(b_num).b_title(b_title).b_content(b_content).b_img(b_img).build());
        }
        return "board/boardview" + b_num;
    }


}
