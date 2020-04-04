package com.spoky.patito.gateway.service;

import java.util.Map;

public interface RunnerService {

    Map<String, Object> getRuntimeResult(String commandLine,
                                                String inputFilePath,
                                                String outputFilePath,
                                                int timeLimit,
                                                int memoryLimit);
    Map<String, Object> runProgram();
}
