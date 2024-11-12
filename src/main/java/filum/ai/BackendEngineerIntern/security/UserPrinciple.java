package filum.ai.BackendEngineerIntern.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import filum.ai.BackendEngineerIntern.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrinciple implements UserDetails {

    private final Integer id;
    private final String username;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(Integer id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrinciple build(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        return new UserPrinciple(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
