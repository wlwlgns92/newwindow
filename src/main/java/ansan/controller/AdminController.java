package ansan.controller;

import ansan.domain.entity.Room.RoomEntity;
import ansan.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @ResponseBody
    @GetMapping("/delete")
    public String delete(@RequestParam("rnum") int rnum) {
        boolean result = roomService.delete(rnum);
        if(result) {
            return "1";
        }else {
            return "2";
        }

    }

    @GetMapping("/activeupdate")
    @ResponseBody
    public String activeupdate(@RequestParam("rnum") int rnum,
            @RequestParam("upactive") String upactive) {
        boolean result = roomService.activeupdate(rnum, upactive);
        if(result) {
            return "1";
        }else {
            return "2";
        }
    }


    //수정 페이지 이동
    @GetMapping("/update{rnum}")
    public String update(@PathVariable("rnum") int rnum, Model model) {

        RoomEntity roomEntity = roomService.getroom(rnum);
        model.addAttribute("roomEntity",roomEntity);
        return "room/roomupdate";
    }

//    // 수정 컨트롤러
//    @PostMapping("/updatecontroller")
//    public String updatecontroller( ){
//
//        return "room/roomlist"
//    }


}
