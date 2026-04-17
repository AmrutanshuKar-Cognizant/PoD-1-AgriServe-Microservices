package com.cognizant.agriserve.feedbackandsatisfactionservice.client;

import com.cognizant.agriserve.feedbackandsatisfactionservice.externaldto.FarmerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="FARMER_SESSION")
public interface FarmerSessionClient {

    @GetMapping("/api/Farmer/{id}")
    FarmerDTO getbyfarmerid(@PathVariable Long id);
}
