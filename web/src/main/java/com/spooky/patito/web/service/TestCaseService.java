package com.spooky.patito.web.service;

import com.spooky.patito.model.sql.TestCase;
import com.spooky.patito.model.transfer.TestCaseDTO;

import java.util.List;

public interface TestCaseService {

    boolean create(TestCaseDTO testCaseDTO);

    boolean update(TestCaseDTO testCaseDTO);

    List<TestCase> getByProblemId(Long problemId);
}
