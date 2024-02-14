package com.solutionchallenge.factchecker.api.Learn.service;

import com.solutionchallenge.factchecker.global.exception.CustomException;
import com.solutionchallenge.factchecker.api.Learn.entity.Word;
import com.solutionchallenge.factchecker.api.Learn.repository.WordRepository;
import com.solutionchallenge.factchecker.api.Learn.dto.response.WordResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//Module Service
@Slf4j
@Service
public class WordService {
    private final WordRepository wordRepository;

    @Autowired
    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<WordResponseDto> getWordList(String member_id) {
        List<Word> words = wordRepository.findAllByMemberIdOrderByCreatedDateDesc(member_id);
        if(words.isEmpty()){
            throw new CustomException("Words not exist");
        }

        // WordResponseDto로 변환하여 반환
        return words.stream()
                .map(WordResponseDto::new) // WordResponseDto를 적절하게 구현해야 합니다.
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    public WordResponseDto updateWordStatus(Long wordId, String member_id) {
        // 해당 유저의 단어 목록에서 wordId에 해당하는 단어 찾기
        Word word = wordRepository.findByWordIdAndMember_Id(wordId, member_id).orElseThrow(()->new CustomException("User not found or word not exist"));
        // 변경된 Word 엔터티를 저장하고 응답
        Word updatedWord = wordRepository.save(word);
        return new WordResponseDto(updatedWord);
    }

    public List<WordResponseDto> getUnknownWordList(String member_id) {
        List<Word> words = wordRepository.findByMember_IdAndKnowStatus(member_id, false);

        // WordResponseDto로 변환하여 반환
        return words.stream()
                .map(WordResponseDto::new)
                .collect(Collectors.toList());


    }
}
