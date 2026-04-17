package com.cognizant.agriserve.feedbackandsatisfactionservice.client;

import com.cognizant.agriserve.feedbackandsatisfactionservice.externaldto.AdvisorySessionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ADVISORY_SESSION")
public interface AdvisorysessionClient {

    @GetMapping("/api/Advisorysession/{id}")
    AdvisorySessionDTO getbyadvisoryid(@PathVariable Long id);
}
