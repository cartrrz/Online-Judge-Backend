package com.spoky.patito.gateway.service.impl;

import com.spoky.patito.gateway.model.Submission;
import com.spoky.patito.gateway.model.transfer.CompileDTO;
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
    public CompileDTO getCompileResult(Submission submission, String workDirectory, String compilePattern){
        String commandLine = getCompileCommandLine(submission.getProblemName(), workDirectory, compilePattern);
        String compileLogPath = String.format("%s/%s-compile.log",workDirectory, submission.getProblemName());

        int timeLimit = 5000;
        int memoryLimit = 0;

        CompileDTO compileDTO = new CompileDTO();
        try {
            Map<String, Object> runningResult = compilerRunner.runProgram(commandLine, "", compileLogPath, timeLimit, memoryLimit);

            boolean success = false;
            if ( runningResult != null ) {
                int exitCode = (Integer)runningResult.get("exitCode");
                success = exitCode == 0;
            }
            compileDTO.setSuccess(success);
            compileDTO.setLog(fileUtil.getContentFile(compileLogPath));

        }catch (Exception e){
            //TODO: loggers
        }


        return compileDTO;
    }

    private String getCompileCommandLine(String name, String workDirectory, String compilePattern) {
        String filePathWithoutExtension = String.format("%s/%s", workDirectory, name);
        String compileCommand = compilePattern
                .replaceAll("\\{filename\\}", filePathWithoutExtension);
        return compileCommand;
    }
}
