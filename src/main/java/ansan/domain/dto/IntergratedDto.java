package ansan.domain.dto;

import ansan.domain.entity.Member.MemberEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
public class IntergratedDto implements UserDetails {

    // 일반회원 + oauth 소셜계정 => 통합 dtd
    private int m_num;
    private String mid;
    private String m_password;
    private final Set<GrantedAuthority> authorities; // 인증

    public IntergratedDto(MemberEntity memberEntity, Collection<? extends GrantedAuthority> authorities) {
        this.mid = memberEntity.getMid();
        this.m_password = memberEntity.getM_password();
        this.m_num = memberEntity.getM_num();
        this.authorities = Collections.unmodifiableSet(new LinkedHashSet<>(sortAuthorities(authorities)));
    }

    private Set<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities ){
        SortedSet<GrantedAuthority> sortAuthorities = new TreeSet<>(Comparator.comparing(GrantedAuthority::getAuthority));
        sortAuthorities.addAll(authorities);
        return sortAuthorities;
    }


    @Override // 회원의 패스워드 반환 메소드
    public String getPassword() {
        return this.m_password;
    }

    @Override // 회원의 Id[이름] 반환 메소드
    public String getUsername() {
        return this.mid;
    }

    @Override // 계정이 만료 여부 확인 [ true 만료되지 않음 ]
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override // 계정이 잠겨있는지 확인 [ true : 잠겨있지 않음 ]
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override // 계정의 패스워드가 만료 여부 확인 [ true : 만료되지 않음 ]
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override // 계정
    public boolean isEnabled() {
        return true;
    }
}
