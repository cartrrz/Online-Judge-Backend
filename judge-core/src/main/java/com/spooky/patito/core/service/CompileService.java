package com.spooky.patito.core.service;

import com.spooky.patito.model.Submission;
import com.spooky.patito.model.transfer.CompileDTO;

public interface CompileService {

    CompileDTO getCompileResult(Submission submission, String workDirectory, String compilePattern);
}
