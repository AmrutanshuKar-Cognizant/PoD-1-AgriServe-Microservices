package com.cognizant.agriserve.feedbackandsatisfactionservice.service.impl;

import com.cognizant.agriserve.feedbackandsatisfactionservice.client.AdvisorysessionClient;
import com.cognizant.agriserve.feedbackandsatisfactionservice.client.FarmerSessionClient;
import com.cognizant.agriserve.feedbackandsatisfactionservice.client.TrainingProgramClient;
import com.cognizant.agriserve.feedbackandsatisfactionservice.dto.FeedbackResponseDTO;
import com.cognizant.agriserve.feedbackandsatisfactionservice.dto.FeedbackRequestDTO;
import com.cognizant.agriserve.feedbackandsatisfactionservice.entity.Feedback;
import com.cognizant.agriserve.feedbackandsatisfactionservice.externaldto.AdvisorySessionDTO;
import com.cognizant.agriserve.feedbackandsatisfactionservice.externaldto.FarmerDTO;
import com.cognizant.agriserve.feedbackandsatisfactionservice.externaldto.TrainingProgramDTO;
import com.cognizant.agriserve.feedbackandsatisfactionservice.repo.Feedbackrepo;
import com.cognizant.agriserve.feedbackandsatisfactionservice.service.FeedbackService;
import com.cognizant.agriserve.feedbackandsatisfactionservice.util.Feedbackutil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class Feedbcakserviceimpl implements FeedbackService {
    @Autowired
    private Feedbackrepo feedbackrepo;
    @Autowired private AdvisorysessionClient advisorysessionClient;
    @Autowired private FarmerSessionClient farmerSessionClient;
    @Autowired private TrainingProgramClient trainingProgramClient;

    public FeedbackResponseDTO addFeedback(FeedbackRequestDTO dto){
        log.info("Recording farmer feedback for session and training program");
        Feedback feedback= Feedbackutil.tofeedback(dto);
        Feedback ft=feedbackrepo.save(feedback);

        AdvisorySessionDTO ad= advisorysessionClient.getbyadvisoryid(dto.getSessionId());
        FarmerDTO fd=farmerSessionClient.getbyfarmerid(dto.getFarmerId());
        TrainingProgramDTO dt=trainingProgramClient.getbyid(dto.getProgramId());

        return FeedbackResponseDTO.builder().FeedbackId(ft.getFeedbackId()).FarmerName(fd.getName()).programName(dt.getProgramName()).rating(ft.getRating()).Comments(ft.getComments()).build();
    }

    public List<FeedbackResponseDTO> getAllFeedback(){
        log.info("Providing each and every record");
        List<Feedback> feedbacks = feedbackrepo.findAll();

        Map<Long, String> farmerNames = new HashMap<>();
        Map<Long, String> programTitles = new HashMap<>();
        return feedbacks.stream().map(f -> {
            // Only call Feign if we haven't seen this ID in this loop yet
            String fName = farmerNames.computeIfAbsent(f.getFarmerId(),
                    id -> farmerSessionClient.getbyfarmerid(id).getName());

            String pTitle = programTitles.computeIfAbsent(f.getTrainingProgramId(),
                    id -> trainingProgramClient.getbyid(id).getProgramName());

            return FeedbackResponseDTO.builder()
                    .FeedbackId(f.getFeedbackId())
                    .rating(f.getRating())
                    .Comments(f.getComments())
                    .FarmerName(fName)
                    .programName(pTitle)
                    .build();
        }).collect(Collectors.toList());
    }
}
