package com.spooky.patito.core.service;

import com.spooky.patito.model.Submission;

import java.util.Map;

public interface JudgeService {

    Map<String, Object> processSubmission(Submission submission);
}
