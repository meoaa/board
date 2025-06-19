package project.board.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.board.member.domain.Member;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(()->member.getRole().name());
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    public long getId(){
        return member.getId();
    }

    public String getNickname(){
        return member.getNickname();
    }
}
