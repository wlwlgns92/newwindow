package ansan.controller;

import ansan.domain.entity.Room.RoomEntity;
import ansan.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/room") // 중복되는 url : 편함
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("/write") // 이동
    public String write(){return "room/roomwrite";}

    @PostMapping("/writecontroller")
    public String writecontroller(RoomEntity roomEntity , @RequestParam("file")List<MultipartFile> files){
        roomService.write(roomEntity, files);
        return "main";
    }
}
