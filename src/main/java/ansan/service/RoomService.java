package ansan.service;

import ansan.domain.dto.MemberDto;
import ansan.domain.entity.Member.MemberEntity;
import ansan.domain.entity.Room.RoomEntity;
import ansan.domain.entity.Room.RoomRepository;
import ansan.domain.entity.Room.RoomimgEntity;
import ansan.domain.entity.Room.RoomimgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    // 저장
    public boolean write(RoomEntity roomEntity , List<MultipartFile> files) {

        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto)session.getAttribute("logindto");
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
        if(files.size() != 0) {
            for(MultipartFile file : files) {
                UUID uuid = UUID.randomUUID();
                uuidfile = uuid.toString()+"_"+file.getOriginalFilename().replaceAll("-","-");

                String dir = "C:\\Users\\505\\newwindow\\src\\main\\resources\\static\\roomimg";
                String filepath = dir + "\\" + uuidfile;
                try {
                    file.transferTo(new File(filepath));
                }catch(Exception e) { System.out.println("파일 저장 실패 : "+ e ); }

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

}
