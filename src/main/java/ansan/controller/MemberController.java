package ansan.controller;


import ansan.domain.dto.MemberDto;
import ansan.service.MemberService;
import ansan.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class MemberController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/member/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/member/signup")
    public String signup() {
        return "member/signup";
    }

    @GetMapping("/member/findid")
    public String findid() {
        return "member/findid";
    }

    @GetMapping("/member/info")
    public String info(Model model) {
        // 로그인 세션 호출
        HttpSession session = request.getSession();
        MemberDto loginDto = (MemberDto)session.getAttribute("logindto");

        // 세션에 회원번호를 service에 전달해서 동일한 회원번호에 회원정보 가져오기
        MemberDto memberDto = memberService.getmemberDto(loginDto.getM_num());
        // 가져온 회원정보를 view에 전달
        model.addAttribute("memberDto", memberDto);

        return "member/info";
    }

    @Autowired
    MemberService memberService;

    @Autowired
    HttpServletRequest request; // 요청객체 jsp : 내장객체

    @PostMapping("/member/signupcontroller") // 회원가입 처리 연결
    public String signupController(MemberDto memberDto,
                                   @RequestParam("address1") String address1,
                                   @RequestParam("address2") String address2,
                                   @RequestParam("address3") String address3,
                                   @RequestParam("address4") String address4) {
        memberDto.setM_address(address1 + "/" + address2 + "/" + address3 + "/" + address4);
        // 자동 중비 : form 입력한 name과 dto의 필드명 동일하면 자동주입
        // 입력이 없는 필드는 초기값 [ 문자 = null, 숫자 = 0]
        memberService.membersignup(memberDto);
        return "redirect:/"; // 회원가입 성공시 로그인페이지 맵핑
    }

    @PostMapping("/member/logincontroller")
    @ResponseBody // 타임임리프를 무시하는 어노테이션
    public String logincontroller(@RequestBody MemberDto memberDto) { // @RequestBody 요청
        // form 사용시에는 Dto에 자동주입이 되지만, json, ajax 사용시 자동주입 x

        MemberDto loginDto = memberService.login(memberDto);
        if (loginDto != null) {
            HttpSession session = request.getSession(); // 서버내 세션 가져오기
            session.setAttribute("logindto", loginDto);
            return "1";
        } else {
            System.out.println("로그인 실패 ");
            return "2";
        }
        // 타임리프를 설치했을경우 return URL, HTML 다른 값을 반환 할때는 @ResponseBody 사용
    }

    @GetMapping("/member/logout")
    public String logout() {
        HttpSession session = request.getSession();
        session.setAttribute("logindto", null);
        return "redirect:/";
    }

    //아이디 찾기
    @PostMapping("/member/findidcontroller")
    public String findidcontroller(MemberDto memberDto, Model model) {
        String result = memberService.findid(memberDto);
        if (result != null) {
            String msg = "회원님의 아이디는 : " + result;
            model.addAttribute("findidmsg", msg);
        } else {
            String msg = "동일한 회원정보가 없습니다.";
            model.addAttribute("findidmsg", msg);
        }
        return "member/findid";
    }

    //비밀번호 찾기
    @PostMapping("/member/findpasswordcontroller")
    public String findpasswordcontroller(MemberDto memberDto, Model model) {
        boolean result = memberService.findpassword(memberDto);
        if (result) {
            String msg = "가입하신 이메일로 임시비밀번호를 발송했습니다.";
            model.addAttribute("findpwmsg", msg);
        } else {
            String msg = "비밀번호를 찾을 수 없습니다.";
            model.addAttribute("findpwmsg", msg);
        }
        return "member/findid";
    }

    // 아이디 중복체크
    @PostMapping("/member/idcheck")
    @ResponseBody
    public int idcheck(@RequestParam("m_id") String m_id) {
        boolean result = memberService.idcheck(m_id);
        if (result) {
            return 1; // 중복
        } else {
            return 2; // 노중복
        }
    }

    @PostMapping("/member/emailcheck")
    @ResponseBody
    public int emailcheck(@RequestParam("m_email") String m_email) {
        boolean result = memberService.emailcheck(m_email);
        if (result) {
            return 1;
        } else {
            return 2;
        }
    }

    // 회원탈퇴
    @GetMapping("/member/mdelete")
    @ResponseBody
    public int mdelete(@RequestParam("passwordconfirm") String passwordconfirm) {

        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto)session.getAttribute("logindto");
        // 로그인된 회원번호와 입력한 패스워드를 service로 보낸다.
        boolean result = memberService.delete(memberDto.getM_num(), passwordconfirm);
        // 결과를 ajax에게 전달
        if(result) {
            return 1;
        }else {
            return 2;
        }
    }
    // 방쪽지 확인 페이지
    @GetMapping("/member/notelist")
    public String notelist( Model model) {
        model.addAttribute("rooms",roomService.getmyroomlist());
        model.addAttribute("notes",roomService.getmynotelist());
        return "member/notelist";
    }

    @GetMapping("/member/notereplywrite")
    @ResponseBody
    public String notereplywrite(@RequestParam("nnum") int nnum,
                                 @RequestParam("nreply") String nreply) {
        roomService.notereplywrite(nnum, nreply);
        return "1";
    }

}
