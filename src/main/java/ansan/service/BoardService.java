package ansan.service;


import ansan.domain.dto.BoardDto;
import ansan.domain.entity.Board.BoardEntity;
import ansan.domain.entity.Board.BoardRepository;
import ansan.domain.entity.Board.ReplyRepository;
import ansan.domain.entity.Reply.ReplyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
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
    // 게시물 리스트 [ 페이징 처리 ]
    public Page<BoardEntity> boardlist (Pageable pageable, String keyword, String search) {
        // pageable = PageRequest.of(1,10); // 2번째 페이지 게시물 10개 출력


        int page = 0;
        if(pageable.getPageNumber() == 0) { // 0이면 1페이지
            page = 0;
        }else {
            page = pageable.getPageNumber()-1; // 1이면 -1 1페이지 2이면 -1 2페이지
        }
        // 페이지 속성 [ PageRequest.of(페이지번호, 페이지 게시물수, 정렬기준)
        pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "bnum")); // 변수 페이지 10개 출력

        //만약에 검색이 있을경우
        if(keyword != null && keyword.equals("b_title")) return boardRepository.findAlltitle(search, pageable);
        if(keyword != null && keyword.equals("b_content")) return boardRepository.findAllcontent(search, pageable);
        if(keyword != null && keyword.equals("b_writer")) return boardRepository.findAllwriter(search, pageable);


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
//                    boardEntity.getBnum(),
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
    @Autowired
    HttpServletRequest request;
    // boardview 출력
    @Transactional  //CRUD중 Read 빼고는 다 넣는다.
    public BoardDto getboard(int b_num) {

        Optional<BoardEntity> entityOptional = boardRepository.findById(b_num);

        String date = entityOptional.get().getCreatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd"));

        // 조회수
        entityOptional.get().setB_view(entityOptional.get().getB_view()+1);
        // 조회수 중복방지 [ 세션을 이용한 ]
        HttpSession session = request.getSession();
        if(session.getAttribute(b_num+"")==null) { // 만약에 기존에 조회수 증가를 안했으면
            // 조회수 변경
            entityOptional.get().setB_view(entityOptional.get().getB_view());
            // 세션부여
            session.setAttribute(b_num+"", 1);
            // 해당 세션 시간 [ 1초 ]
            session.setMaxInactiveInterval(60*60*24);
        }

        return BoardDto.builder()
                .b_createdDate(date)
                .b_num(entityOptional.get().getBnum())
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
            entityOptional.get().setB_img(boardDto.getB_img());
            return true;
        }catch (Exception e) {
            System.out.println("수정실패 : " + e);
            return false;
        }
    }
    @Autowired
    ReplyRepository replyRepository;
    
    //댓글 작성
    public boolean replywrite(int bnum, String rcontent, String rwriter) {
        Optional<BoardEntity> entityOptional = boardRepository.findById(bnum); // 게시물번호에 해당하는 엔티티 출력
        ReplyEntity replyEntity = ReplyEntity.builder()
                .rwriter(rwriter)
                .rcontent(rcontent)
                .boardEntity(entityOptional.get()) // 해당 게시물의 엔티티를 꼭 넣어야 관계맵핑이 성립
                .build();
        replyRepository.save(replyEntity); // 댓글 저장

        // 해당 게시물내 댓글 저장 : 댓글을 게시물에 저장
        entityOptional.get().getReplyEntity().add(replyEntity);
        return true;
    }

    // 모든 댓글 출력
    public List<ReplyEntity> getreplylist(int bnum) {

        // 1. 해당 게시물번호의 엔티티 호출
        Optional<BoardEntity> entityOptional = boardRepository.findById(bnum);
        // 2. 해당 엔티티(댓글번호)의 댓글 리스트 호출
        List<ReplyEntity> replyEntitys = entityOptional.get().getReplyEntity();

        // 정렬후 내림차순
        Collections.reverse(replyEntitys);
        return replyEntitys;
    }

    //댓글 삭제
    public boolean replydelete(int rnum) {
        Optional<ReplyEntity> entityOptional = replyRepository.findById(rnum);
        if(entityOptional != null ) {
            replyRepository.delete(entityOptional.get());
            return true;
        }
        return false;
    }
}
