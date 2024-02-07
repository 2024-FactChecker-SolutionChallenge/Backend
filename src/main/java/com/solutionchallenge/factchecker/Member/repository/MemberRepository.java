package com.solutionchallenge.factchecker.Member.repository;

import com.solutionchallenge.factchecker.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    boolean existsMemberByEmail(String email);
    boolean existsMemberByNickname(String nickname);
}