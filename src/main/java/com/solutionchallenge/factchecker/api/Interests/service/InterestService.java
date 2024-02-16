package com.solutionchallenge.factchecker.api.Interests.service;

import com.solutionchallenge.factchecker.api.Interests.dto.request.SelectedInterestsRequestDto;
import com.solutionchallenge.factchecker.api.Interests.dto.response.SelectedInterestsResponseDto;
import com.solutionchallenge.factchecker.api.Member.entity.Member;
import com.solutionchallenge.factchecker.api.Member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class InterestService {
    private final MemberRepository memberRepository;

    @Autowired
    public InterestService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원가입 시 선택한 키워드를 가져오는 메서드
    public List<String> getSelectedInterests(String memberId) {
        Optional<Member> memberOptional = memberRepository.findMemberById(memberId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            // 사용자의 선택한 키워드가 저장된 Map에서 값들만 추출하여 List로 변환
            List<String> selectedInterests = new ArrayList<>(member.getInterests().values());
            return selectedInterests;
        } else {
            throw new RuntimeException("Member not found with id: " + memberId);
        }
    }

    // 클라이언트가 선택한 키워드를 저장하는 메서드
    // 클라이언트가 선택한 키워드를 저장하는 메서드
    @Transactional
    public SelectedInterestsResponseDto setSelectedInterests(String memberId, SelectedInterestsRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + memberId));

        // 새로운 선택된 관심 분야를 설정
        String updatedSelectedInterests = requestDto.getSelectedInterests().get(0);
        member.setSelectedInterests(updatedSelectedInterests);

        // 업데이트된 선택된 관심 분야를 응답 DTO에 담아서 반환
        return SelectedInterestsResponseDto.builder()
                .memberId(memberId)
                .selectedInterests(updatedSelectedInterests)
                .build();
    }
}
