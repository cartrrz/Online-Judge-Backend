package com.spooky.patito.core.service.impl;

import com.spooky.patito.model.sql.TestCase;
import com.spooky.patito.model.transfer.CompileDTO;
import com.spooky.patito.model.transfer.JudgeDTO;
import com.spooky.patito.core.service.CompileService;
import com.spooky.patito.core.service.JudgeService;
import com.spooky.patito.model.LanguageEnum;
import com.spooky.patito.model.Submission;
import com.spooky.patito.core.service.RunnerService;
import com.spooky.patito.core.util.CompareUtil;
import com.spooky.patito.core.util.FileUtil;
import com.spooky.patito.repository.TestCaseRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.*;

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

    @Autowired
    private TestCaseRepository testCaseRepository;

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
            String baseDirectory = String.format("%s/judge/%s", judgeWorkDirectory, submission.getProblemId());
            //preProcess
            preprocess(submission, baseDirectory, language);
            //Compile
            CompileDTO compileDTO = compileService.getCompileResult(submission, baseDirectory, language.getCompilePatternValue());
            response.put("Compiler",compileDTO);
            //run
            List<JudgeDTO> judgeDTOList = new ArrayList<>();
            if(compileDTO!= null){
                if(compileDTO.isSuccess()){
                    judgeDTOList = getjudgeresult(submission, language.getRunPatternValue(), baseDirectory);
                }
            }
            cleanUp(baseDirectory);
            response.put("RunResponse", judgeDTOList);
            return response;
        }
    }

    public List<JudgeDTO> getjudgeresult(Submission submission, String runPattern, String baseDirectory){
        List<JudgeDTO> judgeDTOList = new ArrayList<>();
        try {
            List<TestCase> testCases = testCaseRepository.findByProblemId(submission.getProblemId());
            for(TestCase testCase : testCases){
                JudgeDTO judgeDTO = new JudgeDTO();
                try{
                    String inputPath = testCase.getInputPath();
                    String outputPath = testCase.getOutputPath();
                    String outputSourcePath = getOutputSourcePath(baseDirectory,testCase.getExtId().toString());
                    String runCommand = getRunCommandLine(submission.getProblemId().toString(), baseDirectory, runPattern);
                    Map<String, Object>runResult = runnerService.runProgram(
                            runCommand,
                            inputPath,
                            outputSourcePath,
                            submission.getTimeLimit(),
                            submission.getMemoryLimit());
                    int exitCode= (Integer)runResult.get("exitCode");
                    int usedTime = (Integer) runResult.get("usedTime");
                    int usedMemory = (Integer) runResult.get("usedMemory");
                    String result = getRunResult(
                            exitCode,
                            submission.getTimeLimit(),
                            usedTime,
                            submission.getMemoryLimit(),
                            usedMemory);
                    if(compareOutput(outputPath,outputSourcePath)){
                        result = "AC";
                    }
                    judgeDTO.setRunresult(result);
                    judgeDTO.setUsedMemory(usedMemory);
                    judgeDTO.setUsedTime(usedTime);
                    judgeDTO.setExtId(testCase.getExtId());
                }catch (Exception e){
                    judgeDTO.setExtId(testCase.getExtId());
                }
                judgeDTOList.add(judgeDTO);
            }

        }catch (Exception e){

        }
        return judgeDTOList;
    }

    private void preprocess(Submission submission, String baseDirectory, LanguageEnum language){

        try{
            File workFile = new File(baseDirectory);
            if ( !workFile.exists() && !workFile.mkdirs() ) {
                throw new Exception("Unable to create a directory: " + baseDirectory);
            }
            setWorkDirectoryPermission(workFile);

            //TODO: change class name in source for java
            createCode(submission.getSource(), submission.getProblemId(),language, baseDirectory);
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

    private boolean createCode(String code, Long problemId, LanguageEnum language, String workDirectory){

        //TODO: I don't know what is a good name maybe a random name generated
        String codeFilePath = String.format("%s/%s.%s",workDirectory, problemId, language.getExtension());
        return fileUtil.createFileWithContent(codeFilePath, code);
    }

    private String getOutputSourcePath(String workDirectory, String tesCaseId){

        String filePathOutput = String.format("%s/output-%s.txt", workDirectory, tesCaseId);
        return filePathOutput;
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

    private void cleanUp(String directory){
        File baseDirFile = new File(directory);
        if ( baseDirFile.exists() ) {
            try {
                FileUtils.deleteDirectory(baseDirFile);
            } catch (IOException ex) {
                //logger
            }
        }
    }
}
