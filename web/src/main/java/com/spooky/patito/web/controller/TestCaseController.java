package com.spooky.patito.web.controller;

import com.spooky.patito.model.transfer.BaseResponse;
import com.spooky.patito.model.transfer.TestCaseDTO;
import com.spooky.patito.web.service.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/testCase")
public class TestCaseController {

    @Autowired
    private TestCaseService testCaseService;


    @PostMapping("create")
    @ResponseBody
    public BaseResponse create(@RequestBody TestCaseDTO testCaseDTO){
        BaseResponse response = new BaseResponse();
        try{
            response.setSuccess(testCaseService.create(testCaseDTO));
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

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
}
