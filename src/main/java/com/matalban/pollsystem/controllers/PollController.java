package com.matalban.pollsystem.controllers;

import com.matalban.pollsystem.api.v0.dto.PollDto;
import com.matalban.pollsystem.services.PollService;
import io.micrometer.common.lang.NonNull;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@Validated
@RestController
@RequestMapping("/rest/api/v0/polls")
public class PollController {

    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @PostMapping()
    public ResponseEntity<PollDto> createPoll(@Valid @RequestBody PollDto pollDto) {
            return new ResponseEntity<>(pollService.createPoll(pollDto), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Page<PollDto>> getPolls(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) {
        return new ResponseEntity<>(pollService.getPollListPage(page,size),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollDto> getPoll(@PathVariable @NonNull Integer id){
        return new  ResponseEntity<>(pollService.getPoll(id),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<PollDto> updatePoll(@PathVariable @NonNull Integer id,
                                              @Valid @RequestBody PollDto pollDto) {
        return new ResponseEntity<>(pollService.updatePoll(id, pollDto), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePoll(@PathVariable  Integer id) {
        pollService.deletePoll(id);
        return  new ResponseEntity<>("Poll with id "+ id.toString() + " deleted",HttpStatus.OK);
    }

}
