package com.spooky.patito.core.service.impl;

import com.spooky.patito.model.transfer.CompileDTO;
import com.spooky.patito.model.transfer.JudgeDTO;
import com.spooky.patito.core.service.CompileService;
import com.spooky.patito.core.service.JudgeService;
import com.spooky.patito.model.LanguageEnum;
import com.spooky.patito.model.Submission;
import com.spooky.patito.core.service.RunnerService;
import com.spooky.patito.core.util.CompareUtil;
import com.spooky.patito.core.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Value("${judge.work.directory}")
    private String judgeWorkDirectory;

    @Autowired
    private CompileService compileService;

    @Autowired
    private RunnerService runnerService;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private CompareUtil compareUtil;

    @Override
    public Map<String, Object> processSubmission(Submission submission){
        synchronized (this){
            LanguageEnum language = null;
            for(LanguageEnum languageEnum: LanguageEnum.values()){
                if(languageEnum.getKey().equalsIgnoreCase(submission.getLanguage())){
                    language = languageEnum;
                }
            }
            Map<String, Object> response = new HashMap<>();
            //TODO: split baseDirectory by diferent sumission with submission id
            String baseDirectory = String.format("%s/judge", judgeWorkDirectory);
            //preProcess
            preprocess(submission, baseDirectory, language);
            //Compile
            CompileDTO compileDTO = compileService.getCompileResult(submission, baseDirectory, language.getCompilePatternValue());
            response.put("Compiler",compileDTO);
            //run
            JudgeDTO judgeDTO = new JudgeDTO();
            if(compileDTO!= null){
                if(compileDTO.isSuccess()){
                    judgeDTO = getjudgeresult(submission, language.getRunPatternValue(), baseDirectory);
                    if(compareOutput(getStdOutputFilePath(baseDirectory),getOutputFilePath(baseDirectory))){
                        judgeDTO.setRunresult("AC");
                    }
                }else{
                    judgeDTO.setRunresult("CE");
                }
            }
            response.put("RunResponse", judgeDTO);
            return response;
        }
    }

    public JudgeDTO getjudgeresult(Submission submission, String runPattern, String baseDirectory){
        JudgeDTO judgeDTO = new JudgeDTO();
        try {
            String inputFilePath=getInputFilePath(baseDirectory);
            String outputFilePath=getOutputFilePath(baseDirectory);
            String runCommand = getRunCommandLine(submission.getProblemName(), baseDirectory, runPattern);
            Map<String, Object> runResult = runnerService.runProgram(
                    runCommand,
                    inputFilePath,
                    outputFilePath,
                    submission.getTimeLimit(),
                    submission.getMemoryLimit());
            int exitCode= (Integer)runResult.get("exitCode");
            int usedTime = (Integer) runResult.get("usedTime");
            int usedMemory = (Integer) runResult.get("usedMemory");
            judgeDTO.setRunresult(getRunResult(
                    exitCode,
                    submission.getTimeLimit(),
                    usedTime,
                    submission.getMemoryLimit(),
                    usedMemory));
            judgeDTO.setUsedMemory(usedMemory);
            judgeDTO.setUsedTime(usedTime);
        }catch (Exception e){

        }
        return judgeDTO;
    }

    private void preprocess(Submission submission, String baseDirectory, LanguageEnum language){

        try{
            File workFile = new File(baseDirectory);
            if ( !workFile.exists() && !workFile.mkdirs() ) {
                throw new Exception("Unable to create a directory: " + baseDirectory);
            }
            setWorkDirectoryPermission(workFile);

            //TODO: change class name in code for java
            createCode(submission.getCode(), submission.getProblemName(),language, baseDirectory);
            createInputAndOutput(submission.getInput(), submission.getStandOutput(), baseDirectory);
        }catch (Exception e){

        }
    }

    private void setWorkDirectoryPermission(File workDirectory) throws IOException {
        if ( !System.getProperty("os.name").contains("Windows") ) {
            Set<PosixFilePermission> permissions = new HashSet<>();

            permissions.add(PosixFilePermission.OWNER_READ);
            permissions.add(PosixFilePermission.OWNER_WRITE);
            permissions.add(PosixFilePermission.OWNER_EXECUTE);

            permissions.add(PosixFilePermission.GROUP_READ);
            permissions.add(PosixFilePermission.GROUP_WRITE);
            permissions.add(PosixFilePermission.GROUP_EXECUTE);

            permissions.add(PosixFilePermission.OTHERS_READ);
            permissions.add(PosixFilePermission.OTHERS_WRITE);
            permissions.add(PosixFilePermission.OTHERS_EXECUTE);
            Files.setPosixFilePermissions(workDirectory.toPath(), permissions);
        }
    }

    private boolean createCode(String code, String problemName, LanguageEnum language, String workDirectory){

        //TODO: I don't know what is a good name maybe a random name generated
        String codeFilePath = String.format("%s/%s.%s",workDirectory, problemName, language.getExtension());
        return fileUtil.createFileWithContent(codeFilePath, code);
    }

    private boolean createInputAndOutput(String input, String output, String workDirectory){

            boolean iscreated = true;

            // Stand Input File
            String filePathInput = getInputFilePath(workDirectory);
            iscreated = iscreated & fileUtil.createFileWithContent(filePathInput, input);

            // Stand Output File
            String filePathOutput = getStdOutputFilePath(workDirectory);
            iscreated = iscreated & fileUtil.createFileWithContent(filePathOutput, output);

            return iscreated;
    }
    private String getInputFilePath(String workDirectory){

        String filePathInput = String.format("%s/input.txt", workDirectory);
        return filePathInput;
    }
    private String getOutputFilePath(String workDirectory){

        String filePathOutput = String.format("%s/output.txt", workDirectory);
        return filePathOutput;
    }

    private String getStdOutputFilePath(String workDirectory){
        String filePathstdOutput = String.format("%s/stdoutput.txt", workDirectory);
        return filePathstdOutput;
    }

    private String getRunCommandLine(String name, String workDirectory, String runPattern) {
        String filePathWithoutExtension = String.format("%s/%s", workDirectory, name);
        String runCommand = runPattern
                .replaceAll("\\{filename\\}", filePathWithoutExtension);
        return runCommand;
    }

    private String getRunResult(int exitCode, int timeLimit, int timeUsed, int memoryLimit, int memoryUsed) {
        if ( exitCode == 0 ) {
            // Output will be compared in next stage
            return "WA";
        }
        if ( timeUsed >= timeLimit ) {
            return "TLE";
        }
        if ( memoryUsed >= memoryLimit ) {
            return "MLE";
        }
        return "RE";
    }

    private boolean compareOutput(String stdoutputFilePath, String outpudFilePath){
        try {
            return compareUtil.isOutputTheSame(stdoutputFilePath, outpudFilePath);
        }catch (Exception e){
            //TODO: logger
        }
        return false;
    }

}
