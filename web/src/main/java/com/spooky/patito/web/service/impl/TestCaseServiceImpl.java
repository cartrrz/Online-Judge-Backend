package com.spooky.patito.web.service.impl;

import com.spooky.patito.core.util.FileUtil;
import com.spooky.patito.model.sql.TestCase;
import com.spooky.patito.model.transfer.TestCaseDTO;
import com.spooky.patito.repository.TestCaseRepository;
import com.spooky.patito.web.service.TestCaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestCaseServiceImpl implements TestCaseService {

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private ServletContext context;

    @Autowired
    private FileUtil fileUtil;

    @Override
    public boolean create(TestCaseDTO testCaseDTO){

        String absolutePath = context.getRealPath("resources/testcase");
        String inputPath = "";
        String outputPath = "";
        //input
        try{
            StringBuffer sb = new StringBuffer();
            sb.append(absolutePath);
            sb.append("/");
            sb.append(testCaseDTO.getProblemId());
            sb.append("/input");
            File workFile = new File(sb.toString());
            if ( !workFile.exists() && !workFile.mkdirs() ) {
                throw new Exception("Unable to create a directory: " + sb.toString());
            }

            sb.append("/");
            sb.append(testCaseDTO.getExtId());
            sb.append(".txt");
            boolean success = fileUtil.createFileWithContent(sb.toString(), testCaseDTO.getInput());
            if(success){
                inputPath = sb.toString();
            }
        }catch (Exception e){
            //logger
        }
        //output
        try{
            StringBuffer sb = new StringBuffer();
            sb.append(absolutePath);
            sb.append("/");
            sb.append(testCaseDTO.getProblemId());
            sb.append("/output");
            File workFile = new File(sb.toString());
            if ( !workFile.exists() && !workFile.mkdirs() ) {
                throw new Exception("Unable to create a directory: " + sb.toString());
            }
            sb.append("/");
            sb.append(testCaseDTO.getExtId());
            sb.append(".txt");
            boolean success = fileUtil.createFileWithContent(sb.toString(), testCaseDTO.getOutput());
            if(success){
                outputPath = sb.toString();
            }
        }catch (Exception e){
            //logger
        }
        if(StringUtils.isNotBlank(inputPath) && StringUtils.isNotBlank(outputPath)){
            TestCase testCase = new TestCase();
            testCase.setExtId(testCaseDTO.getExtId());
            testCase.setProblemId(testCaseDTO.getProblemId());
            testCase.setInputPath(inputPath);
            testCase.setOutputPath(outputPath);
            testCaseRepository.save(testCase);

            return true;
        }
        return false;
    }

    @Override
    public boolean update(TestCaseDTO testCaseDTO){
        TestCase testCase = testCaseRepository.findByExtId(testCaseDTO.getExtId());
        if(testCase != null){
            String inputPath = testCase.getInputPath();
            String outputPath = testCase.getOutputPath();
            if(StringUtils.isBlank(inputPath) || StringUtils.isBlank(outputPath)){
                return false;
            }
            if(fileUtil.createFileWithContent(inputPath, testCaseDTO.getInput()) && fileUtil.createFileWithContent(outputPath, testCaseDTO.getOutput())){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<TestCaseDTO> getByProblemId(Long problemId){
        List<TestCaseDTO> list = new ArrayList<>();
        try{
            List<TestCase> testCases = testCaseRepository.findByProblemId(problemId);
            for(TestCase testCase : testCases){
                TestCaseDTO dto = new TestCaseDTO();
                dto.setExtId(testCase.getExtId());
                dto.setProblemId(testCase.getProblemId());
                list.add(dto);
            }
        }catch (Exception e){
            //logger
        }
        return list;
    }

    @Override
    public TestCaseDTO getByExtId(String extId){
        TestCaseDTO dto = new TestCaseDTO();
        try{
            TestCase testCase = testCaseRepository.findByExtId(extId);
            dto.setExtId(testCase.getExtId());
            dto.setProblemId(testCase.getProblemId());
            dto.setInput(fileUtil.getContentFile(testCase.getInputPath()));
            dto.setOutput(fileUtil.getContentFile(testCase.getOutputPath()));
        }catch(Exception e){
            //logger
        }
        return dto;
    }

    @Override
    public boolean archive(String extId){
        TestCase testCase = testCaseRepository.findByExtId(extId);
        if(testCase != null){
            testCaseRepository.delete(testCase);
            return true;
        }
        return false;
    }

}
