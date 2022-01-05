package ansan.controller;


import ansan.domain.dto.MemberDto;
import ansan.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class MemberController {

    @GetMapping("/member/login")
    public String login() { return "member/login"; }
    @GetMapping("/member/signup")
    public String signup() { return "member/signup"; }


    @Autowired
    MemberService memberService;


    @PostMapping("/member/signupcontroller") // 회원가입 처리 연결
    public String signupController(MemberDto memberDto) {
        // 자동 중비 : form 입력한 name과 dto의 필드명 동일하면 자동주입
            // 입력이 없는 필드는 초기값 [ 문자 = null, 숫자 = 0]
        memberService.membersignup(memberDto);

        return "redirect:/"; // 회원가입 성공시 로그인페이지 맵핑
    }

    @PostMapping("/member/logincontroller")
    public String logincontroller(MemberDto memberDto) {
        MemberDto loginDto = memberService.login(memberDto);
        if(loginDto != null) {
            System.out.println("로그인 성공 ");
        }else {
            System.out.println("로그인 실패 ");
        }
        return "redirect:/";
    }

}
