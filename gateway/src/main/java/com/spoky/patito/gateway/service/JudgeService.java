package com.spoky.patito.gateway.service;

import com.spoky.patito.gateway.model.Submission;

import java.util.Map;

public interface JudgeService {

    Map<String, Object> processSubmission(Submission submission);
}
