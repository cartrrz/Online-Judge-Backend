package com.spooky.patito.core.service.impl;

import com.spooky.patito.core.service.RunnerService;
import com.spooky.patito.core.util.NativeLibraryUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        Map<String, Object> result = new HashMap<>();
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
