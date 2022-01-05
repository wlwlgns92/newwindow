package ansan.service;


import ansan.domain.dto.MemberDto;
import ansan.domain.entity.Member.MemberEntity;
import ansan.domain.entity.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
           if(memberEntity.getM_id().equals(memberDto.getM_id()) &&
           memberEntity.getM_password().equals(memberDto.getM_password()) ) {
            return MemberDto.builder()
                    .m_id(memberEntity.getM_id())
                    .m_num(memberEntity.getM_num() ).build();

           }
        }
        return null;
    }
    
    
}
