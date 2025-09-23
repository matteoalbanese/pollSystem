package com.matalban.pollsystem.controllers;

import com.matalban.pollsystem.api.v0.dto.OptionDto;
import com.matalban.pollsystem.api.v0.dto.VoteDto;
import com.matalban.pollsystem.services.OptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/rest/api/v0/polls")
public class OptionController {

    OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/{id}/options")
    public ResponseEntity<OptionDto> insertOption(@PathVariable Integer id, @RequestBody OptionDto optionDto){
        return new ResponseEntity<>(optionService.insertOption(id, optionDto), HttpStatus.OK);
    }


    @DeleteMapping("/{id}/options/{optionId}")
    public ResponseEntity<String> deleteOption(@PathVariable Integer id, @PathVariable Integer optionId){
        optionService.deleteOption(id, optionId);
        return new ResponseEntity<>("Option with id "+ optionId.toString() + " deleted",HttpStatus.OK);
    }

    @PutMapping("/{id}/options/{optionId}")
    public ResponseEntity<OptionDto> updateOption(@PathVariable Integer id,@PathVariable Integer optionId, @RequestBody OptionDto optionDto){
        return new ResponseEntity<>(optionService.updateOption(id,optionId, optionDto), HttpStatus.OK);
    }

    @GetMapping("/{id}/options/{optionId}")
    public ResponseEntity<OptionDto> getOption(@PathVariable Integer id,@PathVariable Integer optionId){
        return new ResponseEntity<>(optionService.getOption(id,optionId), HttpStatus.OK);
    }

    @PutMapping("/{id}/options/{optionId}/vote")
    public ResponseEntity<OptionDto> vote(@PathVariable Integer id, @PathVariable Integer optionId){
        return new ResponseEntity<>(optionService.vote(id, optionId), HttpStatus.OK);
    }
    
    @GetMapping("/polls/{id}/vote")
    public ResponseEntity<VoteDto> getVote(@PathVariable Integer id){
        return new ResponseEntity<>(optionService.getVote(id), HttpStatus.OK);
    }
}
