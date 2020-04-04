package com.spoky.patito.gateway.service.impl;

import com.spoky.patito.gateway.model.Submission;
import com.spoky.patito.gateway.service.CompileService;
import com.spoky.patito.gateway.service.RunnerService;
import com.spoky.patito.gateway.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class CompileServiceImpl implements CompileService {

    @Autowired
    private RunnerService compilerRunner;

    @Autowired
    private FileUtil fileUtil;

    @Override
    public Map<String, Object> getCompileResult(Submission submission, String workDirectory, String compilePattern){
        String commandLine = getCompileCommandLine(submission.getProblemName(), workDirectory, compilePattern);
        String compileLogPath = String.format("%s/%s-compile.log",workDirectory, submission.getProblemName());

        int timeLimit = 5000;
        int memoryLimit = 0;

        Map<String, Object> runningResult = compilerRunner.getRuntimeResult(commandLine, null, compileLogPath, timeLimit, memoryLimit);

        Map<String, Object> result = new HashMap<>();
        boolean success = false;
        if ( runningResult != null ) {
            int exitCode = (Integer)runningResult.get("exitCode");
            success = exitCode == 0;
        }
        result.put("Success", success);
        result.put("log", fileUtil.getContentFile(compileLogPath));

        return null;
    }

    private String getCompileCommandLine(String name, String workDirectory, String compilePattern) {
        String filePathWithoutExtension = String.format("%s/%s", workDirectory, name);
        String compileCommand = compilePattern
                .replaceAll("\\{filename\\}", filePathWithoutExtension);
        return compileCommand;
    }
}
