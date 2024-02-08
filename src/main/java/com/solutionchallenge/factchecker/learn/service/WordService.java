package com.solutionchallenge.factchecker.learn.service;

import com.solutionchallenge.factchecker.learn.domain.QuizWordRepository;
import com.solutionchallenge.factchecker.learn.domain.Word;
import com.solutionchallenge.factchecker.learn.domain.WordRepository;
import com.solutionchallenge.factchecker.learn.dto.response.WordResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//Module Service
@Service
public class WordService {
    private final UserRepository userRepository;
    private final WordRepository wordRepository;

    @Autowired
    public WordService(UserRepository userRepository, WordRepository wordRepository) {
        this.userRepository = userRepository;
        this.wordRepository = wordRepository;
    }

    public List<WordResponseDto> getWordList(Long userId) {
        List<Word> words = wordRepository.findAllByUserId(userId);

        // WordResponseDto로 변환하여 반환
        return words.stream()
                .map(WordResponseDto::new) // WordResponseDto를 적절하게 구현해야 합니다.
                .collect(Collectors.toList());
    }

    public WordResponseDto updateWordStatus(Long wordId, Long userId) {
        // 해당 유저의 단어 목록에서 wordId에 해당하는 단어 찾기
        Optional<Word> optionalWord = wordRepository.findByIdAndUser_WordId(wordId, userId);
        if (optionalWord.isPresent()) {
            // 해당하는 단어를 찾았으면 상태 업데이트
            Word word = optionalWord.get();
            word.updateWord(word.getWord(), word.getMean(), !word.isKnowStatus());
            // 변경된 Word 엔터티를 저장하고 응답
            Word updatedWord = wordRepository.save(word);
            return new WordResponseDto(updatedWord);
        } else {
            //throw new CustomException("Word not found or does not belong to the User");
        }
    }
    public List<WordResponseDto> getUnknownWordList(Long userId) {
        List<Word> words = wordRepository.findByUserIdAndKnowStatus(userId, false);


        // WordResponseDto로 변환하여 반환
        return words.stream()
                .map(WordResponseDto::new)
                .collect(Collectors.toList());


    }
}
