package com.spoky.patito.gateway.service.impl;

import com.spoky.patito.gateway.model.Submission;
import com.spoky.patito.gateway.service.RunnerService;
import com.spoky.patito.gateway.util.NativeLibraryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RunnerServiceImpl implements RunnerService {

    @Value("${system.username}")
    private String systemUsername;

    @Value("${system.password}")
    private String systemPassword;

    @Override
    public Map<String, Object> runProgram(String commandLine,
                                                String inputFilePath,
                                                String outputFilePath,
                                                int timeLimit,
                                                int memoryLimit) {
        Map<String, Object> result = null;
        try {
            result = getRuntimeResult(commandLine, systemUsername, systemPassword,
                    inputFilePath, outputFilePath, timeLimit, memoryLimit);
        } catch ( Exception e ) {
            //do logger
        }
        return result;
    }



    private native Map<String, Object> getRuntimeResult(String commandLine, String systemUsername, String systemPassword, String inputFilePath, String outputFilePath, int timeLimit, int memoryLimit);

    static {
        try {
            NativeLibraryUtil.loadLibrary("JudgerCore");
        } catch (Exception e) {
            e.printStackTrace();
            //do logger
        }
    }

}
