package ansan.service;


import ansan.domain.dto.IntergratedDto;
import ansan.domain.dto.MemberDto;
import ansan.domain.entity.Member.MemberEntity;
import ansan.domain.entity.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MemberService implements UserDetailsService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    HttpServletRequest request;
    // 회원등록 메소드
    public boolean membersignup(MemberDto memberDto) {
        // 패스워드 암호화 [ BCryptPasswordEncoder ]
        // 1. 암호화 클래스 객체 생성
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 2. 입력받은 memberDto내 패스워드 재설정 [ 암호화객체명.encode(입력받은 패스워드)
        memberDto.setM_password(passwordEncoder.encode(memberDto.getM_password() ) );

        memberRepository.save(memberDto.toentity()); // save(entity) : insert , update
        return true;
    }

//    //회원검사 메소드
//    public MemberDto login(MemberDto memberDto) {
//        List<MemberEntity> memberEntityList = memberRepository.findAll();
//
//        for (MemberEntity memberEntity : memberEntityList) {
//            if (memberEntity.getMid().equals(memberDto.getM_id()) &&
//                    memberEntity.getM_password().equals(memberDto.getM_password())) {
//                return MemberDto.builder()
//                        .m_id(memberEntity.getMid())
//                        .m_num(memberEntity.getM_num()).build();
//
//            }
//        }
//        return null;
//    }

    // 회원 아이디 찾기
    public String findid(MemberDto memberDto) {
        // 1. 모든 엔티티 호출
        List<MemberEntity> memberEntities = memberRepository.findAll();
        // 반복문 이용한 모든 엔티티를 하나씩 꺼내보기
        for (MemberEntity memberEntity : memberEntities) {
            // 만약에 해당 엔티티가 이름과 이메일이 동일하면
            if (memberEntity.getM_name().equals(memberDto.getM_name()) &&
                    memberEntity.getMemail().equals(memberDto.getM_email())) {
                return memberEntity.getMid(); // 아이디를 반환
            }
        }
        return null; // 못찾았을때
    }
    @Autowired
    private JavaMailSender javaMailSender; // 자바 메일 객체 [ 라이브러리 필수 ]
    // 회원 비밀번호 찾기 --> 임시비밀번호 메일전송
    @Transactional
    public boolean findpassword(MemberDto memberDto) {
        // 1. 모든 엔티티 호출
        List<MemberEntity> memberEntities = memberRepository.findAll();
        // 반복문 이용한 모든 엔티티를 하나씩 꺼내보기
        for (MemberEntity memberEntity : memberEntities) {
            // 만약에 해당 엔티티가 이름과 이메일이 동일하면
            if (memberEntity.getMid().equals(memberDto.getM_id()) &&
                    memberEntity.getMemail().equals(memberDto.getM_email())) {

                String from = "slal4952@naver.com"; // 보내는사람
                String to = memberEntity.getMemail(); // 받는 사람
                String subject = "Ansan 계정 임시 비밀번호 발송"; // 제목
                StringBuilder body = new StringBuilder(); // StringBuilder 문자열 연결 클래스 [ 문자열1 + 문자열 2 ]
                body.append("<html> <body> <h1>Ansan 계정 임시 비밀번호<h1>"); // 보내는 메시지에 html 추가

                Random random = new Random();
                // 임시 비밀번호 만들기

                StringBuilder temppassword = new StringBuilder(); // + 역할
                for(int i = 0; i< 12; i++) {
                    // 랜덤 숫자  = > 문자변환
                    temppassword.append((char)((int)(random.nextInt(+26))+97));
                }
                body.append("<div>"+temppassword+"</div></html>"); // 보내는 메시지에 임시비밀번호를 html에 추가

                 // 엔티티내 패스워드 변경
                memberEntity.setM_password(temppassword.toString()); // JPA
                // Mime : 전자우편 프로토콜 [ 통신 규약 ]
                // SMTP : 전자우편 전송 프로토콜 [ 통신 규칙 ]
                // HTTP : 문서전송 프로토콜
                try {
                    MimeMessage message = javaMailSender.createMimeMessage();
                    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "utf-8");
                    mimeMessageHelper.setFrom(from, "Ansan");
                    mimeMessageHelper.setTo(to);
                    mimeMessageHelper.setSubject("Ansan 계정 임시 비밀번호 발송");
                    mimeMessageHelper.setText(body.toString(), true);
                    javaMailSender.send(message);
                }
                catch (Exception e) { System.out.println("메일전송 실패 " + e); }
                return true; // 아이디를 반환
            }
        }
        return false;
    }

    // 아이디 중복체크
    public boolean idcheck ( String m_id ) {
        //모든 엔티티 가져오기
        List<MemberEntity> memberEntities = memberRepository.findAll();
        // 모든 엔티티 반복문 돌려서 엔티티 하나씩 가져오기
        for(MemberEntity memberEntity : memberEntities) {
            //만약에  해당 엔티티가 입력한 아이디와 동일하면
            if(memberEntity.getMid().equals(m_id)) {
                return true; // 중복
            }
        }
        return false; // 중복없음
    }

    // 이메일 중복체크
    public boolean emailcheck ( String m_email) {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        for(MemberEntity memberEntity : memberEntities) {
            if(memberEntity.getMemail().equals(m_email)) {
                return true;
            }
        }
        return false;
    }

    // 회원번호 -> 회원정보 반환
    public MemberDto getmemberDto(int m_num) {
        // memberRepository.findAll(); 모든 엔티티 호출
        // memberRepository.findById(pk값); 해당 pk값의 엔티티 호출
        // 해당 회원번호[pk]만 엔티티 호출
        Optional<MemberEntity> memberEntity = memberRepository.findById(m_num);

        // 찾은 엔티티를 dto로 변경후 반환
        MemberDto memberDto = new MemberDto();
        return memberDto.builder()
                .m_id(memberEntity.get().getMid())
                .m_email(memberEntity.get().getMemail())
                .m_grade(memberEntity.get().getM_grade())
                .m_name(memberEntity.get().getM_name())
                .m_phone(memberEntity.get().getM_phone())
                .m_sex(memberEntity.get().getM_sex())
                .m_point(memberEntity.get().getM_point())
                .m_address(memberEntity.get().getM_address())
                .m_createdDate(memberEntity.get().getCreatedDate()).build();
    }

    // 회원탈퇴
    public boolean delete(int m_num, String passwordconfirm) {
        // 로그인된 회원번호의 엔티티 호출
        Optional<MemberEntity> memberEntity = memberRepository.findById(m_num);
        // Optional 클래스 null 포함 객체 저장
            // memberEntity.get() : Optional 내 객체 호출
        // 해당 엔티티내 패스워드가 확인 패스워드와 같다면
        if(memberEntity.get().getM_password().equals(passwordconfirm)) {
            memberRepository.delete(memberEntity.get());
            return true; // 회원탈퇴
        }else {
            return false; // 다르면 false
        }
    }

    // 회원번호 -> 회원 엔티티 반환
    public MemberEntity getmentity(int mnum){
        Optional<MemberEntity> entityOptional = memberRepository.findById(mnum);
        return entityOptional.get();
    }


    @Override // 로그인처리 /member/logincontroller  URL 호출시 실행되는 메소드 [ 로그인 처리(인증처리) 메소드 ]
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {
        Optional<MemberEntity> entityOptional = memberRepository.findBymid(mid);
        MemberEntity memberEntity = entityOptional.orElse(null);
                                                    // orElse(null) 만ㅇ약에 엔티티가 없으면 null
        //찾은 회원엔티티의 권한[키]을 리스트에 담기
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(memberEntity.getRoleKey() ) );
        
        // 세션 부여
        MemberDto loginDto = MemberDto.builder()
                .m_id(memberEntity.getMid())
                .m_num(memberEntity.getM_num())
                .build();

            HttpSession session = request.getSession(); // 서버내 세션 가져오기
            session.setAttribute("logindto", loginDto);


        return new IntergratedDto(memberEntity, authorities);
    }
}
