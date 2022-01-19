package ansan.controller;

import ansan.domain.entity.Room.RoomEntity;
import ansan.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    RoomService roomService;

    @GetMapping("/roomlist")
    public String roomlist(Model model) {
        List<RoomEntity> roomEntities = roomService.getroomlist();
        model.addAttribute("roomEntities", roomEntities);
        return "admin/roomlist";
    }

}
