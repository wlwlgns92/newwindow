package ansan.service;


import ansan.domain.dto.MemberDto;
import ansan.domain.entity.Member.MemberEntity;
import ansan.domain.entity.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    // 회원등록 메소드
    public boolean membersignup(MemberDto memberDto) {
        memberRepository.save(memberDto.toentity()); // save(entity) : insert , update
        return true;
    }

    //회원검사 메소드
    public MemberDto login(MemberDto memberDto) {
        List<MemberEntity> memberEntityList = memberRepository.findAll();

        for (MemberEntity memberEntity : memberEntityList) {
            if (memberEntity.getM_id().equals(memberDto.getM_id()) &&
                    memberEntity.getM_password().equals(memberDto.getM_password())) {
                return MemberDto.builder()
                        .m_id(memberEntity.getM_id())
                        .m_num(memberEntity.getM_num()).build();

            }
        }
        return null;
    }

    // 회원 아이디 찾기
    public String findid(MemberDto memberDto) {
        // 1. 모든 엔티티 호출
        List<MemberEntity> memberEntities = memberRepository.findAll();
        // 반복문 이용한 모든 엔티티를 하나씩 꺼내보기
        for (MemberEntity memberEntity : memberEntities) {
            // 만약에 해당 엔티티가 이름과 이메일이 동일하면
            if (memberEntity.getM_name().equals(memberDto.getM_name()) &&
                    memberEntity.getM_email().equals(memberDto.getM_email())) {
                return memberEntity.getM_id(); // 아이디를 반환
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
            if (memberEntity.getM_id().equals(memberDto.getM_id()) &&
                    memberEntity.getM_email().equals(memberDto.getM_email())) {

                String to = "slal4952@naver.com"; // 보내는사람
                String from = "wlwlgns92@gmail.com"; // 받는 사람
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
                    mimeMessageHelper.setFrom("slal4952@naver.com", "Ansan");
                    mimeMessageHelper.setTo("slal4952@naver.com");
                    mimeMessageHelper.setSubject("Ansan 계정 임시 비밀번호 발송");
                    mimeMessageHelper.setText(body.toString(), true);
                    javaMailSender.send(message);
                }
                catch (Exception e) { System.out.println("메일전송 실패 "); }
                return true; // 아이디를 반환
            }
        }
        return false;
    }
}
