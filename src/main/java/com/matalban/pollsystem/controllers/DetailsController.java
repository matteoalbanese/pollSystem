package com.matalban.pollsystem.controllers;

import com.matalban.pollsystem.api.v0.dto.PollDetails;
import com.matalban.pollsystem.services.DetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/api/v0")
public class DetailsController {

    private final DetailsService detailsService;

    public DetailsController(DetailsService detailsService) {
        this.detailsService = detailsService;
    }

    @GetMapping("/polls-details/{id}")
    public PollDetails getPollDetails(@PathVariable Integer id) {
        System.out.println("getPollDetails controller");
       return detailsService.getPollDetails(id);
    }
}
