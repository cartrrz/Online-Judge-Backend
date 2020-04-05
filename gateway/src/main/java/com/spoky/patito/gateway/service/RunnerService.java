package com.spoky.patito.gateway.service;

import java.util.Map;

public interface RunnerService {

    Map<String, Object> runProgram(String commandLine,
                                                String inputFilePath,
                                                String outputFilePath,
                                                int timeLimit,
                                                int memoryLimit);

}
