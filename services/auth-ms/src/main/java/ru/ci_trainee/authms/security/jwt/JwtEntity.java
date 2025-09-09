package ru.ci_trainee.authms.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.ci_trainee.authms.model.Role;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class JwtEntity implements UserDetails {
    private UUID id;
    private String username;
    private String password;
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }
}
