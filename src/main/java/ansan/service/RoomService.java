package ansan.service;

import ansan.domain.dto.MemberDto;
import ansan.domain.entity.Member.MemberEntity;
import ansan.domain.entity.Room.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Service // 서비스와 컨트롤을 연결
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RoomimgRepository roomimgRepository;
    @Autowired
    private NoteRepository noteRepository;

    // 저장
    public boolean write(RoomEntity roomEntity, List<MultipartFile> files) {

        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        // 회원번호 -> 회원 엔티티 가져오기
        MemberEntity memberEntity = memberService.getmentity(memberDto.getM_num());
        // 룸 엔티티에 회원 엔티티 넣기
        roomEntity.setMemberEntity(memberEntity);
        // 방상태 : 첫 등록시 검토중으로 설정
        roomEntity.setRactive("검토중");
        int rnum = roomRepository.save(roomEntity).getRnum();
        // 회원 엔티티에 룸 엔티티 넣기
        RoomEntity roomEntitysaved = roomRepository.findById(rnum).get();
        memberEntity.getRoomEntities().add(roomEntitysaved);

        //파일처리
        String uuidfile = null;
        if (files.size() != 0) {
            for (MultipartFile file : files) {
                UUID uuid = UUID.randomUUID();
                uuidfile = uuid.toString() + "_" + file.getOriginalFilename().replaceAll("-", "-");

                String dir = "C:\\Users\\505\\newwindow\\src\\main\\resources\\static\\roomimg";
                String filepath = dir + "\\" + uuidfile;
                try {
                    file.transferTo(new File(filepath));
                } catch (Exception e) {
                    System.out.println("파일 저장 실패 : " + e);
                }

                // roomimg 엔티티 생성 [ 해당 room 엔티티 주입 ] 
                RoomimgEntity roomimgEntity = RoomimgEntity.builder().rimg(uuidfile).roomEntity(roomEntitysaved).build();
                // room 엔티티내 roomimg 리스트에 roomimg 인티티 주입
                int roomimgnum = roomimgRepository.save(roomimgEntity).getRimgnum();
                roomEntitysaved.getRoomimgEntities().add(roomimgRepository.findById(roomimgnum).get());

            }
        }
        return true;
    }

    // 모든 room 가져오기
    public List<RoomEntity> getroomlist() {
        return roomRepository.findAll();
    }

    // 특정 룸 가져오기
    public RoomEntity getroom(int rnum) {
        return roomRepository.findById(rnum).get();
    }

    // 특정 room  삭제
    public boolean delete(int rnum) {
        roomRepository.delete(roomRepository.findById(rnum).get());
        return true;
    }

    // 특정 룸 상태변경
    @Transactional
    public boolean activeupdate(int rnum, String upactive) {
        RoomEntity roomEntity = roomRepository.findById(rnum).get();
        if (roomEntity.getRactive().equals(upactive)) {
            return false; // 선택 버튼의 상태와 기존 룸상태가 같으면 업데이트 x
        } else {
            roomEntity.setRactive(upactive);
            return true;
        }
    }

    // 수정

    //문의 등록
    public boolean notewrite(int rnum, String ncontents) {

        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        if (memberDto == null) {
            return false;
        }
        // 문의 엔티티 생성
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setNcontent(ncontents);
        noteEntity.setMemberEntity(memberService.getmentity(memberDto.getM_num()));
        noteEntity.setRoomEntity(roomRepository.findById(rnum).get());

        //문의 엔티티 저장
        int nnum = noteRepository.save(noteEntity).getNnum();
        // 해당 룸엔티티의 문의리스트에 문의 엔티티 저장
        roomRepository.findById(rnum).get().getNoteEntities().add(noteRepository.findById(nnum).get());
        // 해당 회원엔티티의 문의리스트에 문의 엔티티 저장
        memberService.getmentity(memberDto.getM_num()).getNoteEntities().add(noteRepository.findById(nnum).get());

        return true;
    }

    // 로그인된 회원이 등록한 방 출력
    public List<RoomEntity> getmyroomlist() {
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        MemberEntity memberEntity = memberService.getmentity(memberDto.getM_num());
        return memberEntity.getRoomEntities();
    }

    // 로그인된 회원이 등록한 문의 출력
    public List<NoteEntity> getmynotelist() {
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        MemberEntity memberEntity = memberService.getmentity(memberDto.getM_num());
        return memberEntity.getNoteEntities();
    }

    //답변등록
    @Transactional
    public boolean notereplywrite(int nnum, String nreply){
        noteRepository.findById(nnum).get().setNreply(nreply);
        return true;
    }
    // read : 0 안읽음 1 : 읽음
    public void nreadcount() {
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto)session.getAttribute("logindto");
        if(memberDto == null ) return;

        int nreadcount = 0; // 안읽은 쪽지의 개수

        // 로그인된 회원번호와 쪽지 받은 사람의 회원번호가 모두 동일하면면
       for(NoteEntity noteEntity : noteRepository.findAll()){
            if(noteEntity.getRoomEntity().getMemberEntity().getM_num() == memberDto.getM_num()
            && noteEntity.getNread() == 0) { nreadcount++; }
        }
       // 세션에 저장
        session.setAttribute("nreadcount", nreadcount);
    }

    // 읽음 처리 서비스
    @Transactional
    public boolean nreadupdate(int nnum) {
        noteRepository.findById(nnum).get().setNread(1);
        return true;
    }

}
