package com.solutionchallenge.factchecker.learn.controller;

import com.solutionchallenge.factchecker.learn.service.LearnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
    private final LearnService learnService;

    // TODO: 주간뉴스조회, 데일리퀴즈점수 매주 월요일 자정 0으로 초기화


    // TODO: 데일리퀴즈도전기회 - 매일 자정 1로 초기화


}
