package com.spooky.patito.web.controller;

import com.spooky.patito.model.sql.TestCase;
import com.spooky.patito.model.transfer.BaseResponse;
import com.spooky.patito.model.transfer.ListResponse;
import com.spooky.patito.model.transfer.ObjectResponse;
import com.spooky.patito.model.transfer.TestCaseDTO;
import com.spooky.patito.repository.TestCaseRepository;
import com.spooky.patito.web.service.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/testCase")
public class TestCaseController {

    @Autowired
    private TestCaseService testCaseService;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @PostMapping()
    @ResponseBody
    public BaseResponse createOrUpdate(@RequestBody TestCaseDTO testCaseDTO){
        BaseResponse response = new BaseResponse();
        try{
            TestCase testCase = testCaseRepository.findByExtId(testCaseDTO.getExtId());
            if(testCase == null){
                response.setSuccess(testCaseService.create(testCaseDTO));
                if(response.isSuccess()){
                    response.setNewTestCase(true);
                }
            }else{
                response.setSuccess(testCaseService.update(testCaseDTO));
            }
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Deprecated
    @PostMapping("/update")
    @ResponseBody
     public BaseResponse update(@RequestBody TestCaseDTO testCaseDTO){
        BaseResponse response = new BaseResponse();
        try{
            response.setSuccess(testCaseService.update(testCaseDTO));
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/archive/{extId}")
    @ResponseBody
    public BaseResponse archive(@PathVariable("extId") Long extId){
        BaseResponse response = new BaseResponse();
        try {
            response.setSuccess(testCaseService.archive(extId));
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/archive/problem/{problemId}")
    @ResponseBody
    public BaseResponse archiveProblem(@PathVariable("problemId") Long problemId){
        BaseResponse response = new BaseResponse();
        try{
            List<Long> extIds = testCaseRepository.findByProblemId(problemId)
                    .stream()
                    .map(TestCase::getExtId)
                    .collect(Collectors.toList());
            List<BaseResponse> list = new ArrayList<>();
            for(Long extId : extIds){
                if(!testCaseService.archive(extId)){
                    response.setSuccess(false);
                    response.setMessage("Unable to archive testCase extId : "+ extId);
                    break;
                }
            }

        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @GetMapping("/problem/{problemId}")
    @ResponseBody
    public ListResponse<TestCaseDTO> testCaseByProblem(@PathVariable("problemId") Long problemId){
        ListResponse<TestCaseDTO> response = new ListResponse();
        try{
            List<TestCaseDTO> list = testCaseService.getByProblemId(problemId);
            response.setResponse(list);
            response.setTotal(list.size());
            response.setSuccess(true);
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return  response;
    }

    @GetMapping("{extId}")
    @ResponseBody
    public ObjectResponse<TestCaseDTO> testCaseByExtId(@PathVariable("extId") Long extId){
        ObjectResponse<TestCaseDTO> response = new ObjectResponse<>();
        try{
            TestCaseDTO dto = testCaseService.getByExtId(extId);
            response.setResponse(dto);
            response.setSuccess(true);
        }catch(Exception e){
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return response;
    }
}
