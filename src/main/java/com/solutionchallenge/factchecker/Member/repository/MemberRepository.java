package com.solutionchallenge.factchecker.Member.repository;

import com.solutionchallenge.factchecker.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}