package com.spoky.patito.gateway.service;

import com.spoky.patito.gateway.model.Submission;
import com.spoky.patito.gateway.model.transfer.CompileDTO;

import java.util.Map;

public interface CompileService {

    CompileDTO getCompileResult(Submission submission, String workDirectory, String compilePattern);
}
