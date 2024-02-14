package com.solutionchallenge.factchecker.api.Youtube.repository;

import com.solutionchallenge.factchecker.api.Youtube.entity.Youtube;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YoutubeRepository extends JpaRepository<Youtube,Long> {
}
