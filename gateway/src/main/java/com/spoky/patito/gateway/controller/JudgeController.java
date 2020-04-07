package com.spoky.patito.gateway.controller;

import com.spoky.patito.gateway.service.JudgeService;
import com.spoky.patito.gateway.model.LanguageEnum;
import com.spoky.patito.gateway.model.Submission;
import com.spoky.patito.gateway.model.transfer.SubmissionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/judge")
public class JudgeController {

    @Autowired
    private JudgeService judgeService;

    @GetMapping("/dummy")
    public String dummy(){
        return "carlos test";
    }
    @PostMapping("/submit")
    public SubmissionResponse submitProblem(@RequestBody Submission submission){
        SubmissionResponse response = new SubmissionResponse();
        try {
            boolean valid = false;
            for(LanguageEnum languageEnum : LanguageEnum.values()){
                valid = valid | (languageEnum.getKey().equalsIgnoreCase(submission.getLanguage()));
            }
            if(valid){
                Map<String, Object> map = judgeService.processSubmission(submission);
                response.setResponse(map);
                response.setSuccess(true);
            }else{
                response.setSuccess(false);
                response.setMessage("Invalid or not supported Language");
            }
        }catch(Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }

        return response;
    }

}
