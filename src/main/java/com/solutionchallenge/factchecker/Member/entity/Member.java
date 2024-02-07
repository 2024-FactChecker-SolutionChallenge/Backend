package com.solutionchallenge.factchecker.Member.entity;
import com.solutionchallenge.factchecker.global.entity.DateEntity;
 import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "User")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends DateEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;


    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "nick_name")
    @NotNull
    private String nickname;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "grade")
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Grade grade;

    @Type(type = "json")
    @Column(name = "user_interests", columnDefinition = "json")
    private Map<String, String> interests = new HashMap<>();


    public Long getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public Grade getGrade() {
        return grade;
    }

    public Map<String, String> getInterests() {
        return interests;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
         return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
