package com.solutionchallenge.factchecker.Member.entity;
import com.solutionchallenge.factchecker.global.entity.DateEntity;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
@Entity
@Table(name = "User")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends DateEntity {
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

    @Column(name = "email_verification_code")
    @NotNull
    private String emailVerificationCode;

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

}
