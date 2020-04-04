package com.spoky.patito.gateway.service;

import com.spoky.patito.gateway.model.Submission;

import java.util.Map;

public interface CompileService {

    Map<String, Object> getCompileResult(Submission submission, String workDirectory, String compilePattern);
}
