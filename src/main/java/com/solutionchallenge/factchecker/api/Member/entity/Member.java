package com.solutionchallenge.factchecker.api.Member.entity;
import com.solutionchallenge.factchecker.global.entity.BaseTimeEntity;
import com.sun.istack.NotNull;
import com.vladmihalcea.hibernate.type.json.JsonType;
import io.swagger.models.auth.In;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonType.class)
})
public class Member extends BaseTimeEntity implements UserDetails {

    @Id
    @Column(name = "member_id")
    private String id;

    private String password;
    private String nickname;

    @Column(name = "grade")
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Grade grade;
//
//    @Column(name = "interests")
//    @NotNull
//    @Enumerated(EnumType.ORDINAL)
//    private Interests interests;
//
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, String> interests = new HashMap<>();


    // 회원가입용
    @Builder
    public Member(String id, String password, String nickname, Grade grade , Map<String , String> interests) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.grade = grade;
        this.interests = interests;
//        this.interests = interests;

      }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.id;
    }




    // 계정 만료되었는지 (true - 만료 안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠겨있는지 (true - 안잠김)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 비밀번호 만료되었는지 (true - 만료 X)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 상태인지 (true - 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

}
