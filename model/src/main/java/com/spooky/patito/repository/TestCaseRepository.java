package com.spooky.patito.repository;

import com.spooky.patito.model.sql.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    List<TestCase> findByProblemId(Long problemId);

    TestCase findByExtId(Long extId);
}
