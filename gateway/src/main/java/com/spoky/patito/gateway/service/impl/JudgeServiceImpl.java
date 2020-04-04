package com.spoky.patito.gateway.service.impl;

import com.spoky.patito.gateway.service.CompileService;
import com.spoky.patito.gateway.service.JudgeService;
import com.spoky.patito.gateway.model.LanguageEnum;
import com.spoky.patito.gateway.model.Submission;
import com.spoky.patito.gateway.util.FileUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Value("${judge.work.directory}")
    private String judgeWorkDirectory;

    @Autowired
    private CompileService compileService;

    @Autowired
    private FileUtil fileUtil;

    @Override
    public Map<String, Object> processSubmission(Submission submission){
        synchronized (this){
            LanguageEnum language = null;
            for(LanguageEnum languageEnum: LanguageEnum.values()){
                if(languageEnum.getKey().equalsIgnoreCase(submission.getLanguage())){
                    language = languageEnum;
                }
            }
            //TODO: split baseDirectory by diferent sumission with submission id
            String baseDirectory = String.format("%s/judge", judgeWorkDirectory);
            //preProcess
            preprocess(submission, baseDirectory, language);
            //Compile
            Map<String,Object>compileResult = compileService.getCompileResult(submission, baseDirectory, language.getCompilePatternValue());
            //run

            //compare

            //response
            return null;
        }
    }

    private void preprocess(Submission submission, String baseDirectory, LanguageEnum language){

        try{
            File workFile = new File(baseDirectory);
            if ( !workFile.exists() && !workFile.mkdirs() ) {
                throw new Exception("Unable to create a directory: " + baseDirectory);
            }
            //TODO: change class name in code for java
            createCode(submission.getCode(), submission.getProblemName(),language, baseDirectory);
            createInputAndOutput(submission.getInput(), submission.getStandOutput(), baseDirectory);
        }catch (Exception e){

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
            String filePathInput = String.format("%s/input.txt", workDirectory);
            iscreated = iscreated & fileUtil.createFileWithContent(filePathInput, input);

            // Stand Output File
            String filePathOutput = String.format("%s/stdoutput.txt", workDirectory);
            iscreated = iscreated & fileUtil.createFileWithContent(filePathOutput, output);

            return iscreated;
    }

}
