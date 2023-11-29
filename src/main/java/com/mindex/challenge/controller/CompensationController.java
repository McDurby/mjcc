package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
class CompensationController {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    @GetMapping("/employee/{id}/compensation")
    public ResponseEntity<Compensation> get(@PathVariable String id) {
        LOG.debug("Retrieve employee compensation for [{}]", id);

        Optional<Compensation> compensation = compensationService.read(id);
        if (compensation.isPresent()) {
            return new ResponseEntity<>(compensation.get(), HttpStatus.OK);
        }

        LOG.debug("Failed to find employee compensation for [{}]", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/employee/{id}/compensation")
    public ResponseEntity<Compensation> create(@PathVariable String id, @RequestBody Compensation compensation) {
        LOG.debug("Received employee compensation create request for ID [{}] and compensation [{}]", id, compensation.getEmployee());

        Optional<Compensation> comp = compensationService.create(id, compensation);
        if (comp.isPresent()) {
            return new ResponseEntity<>(comp.get(), HttpStatus.OK);
        }

        LOG.debug("Failed to find employee with ID [{}]", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/employee/compensations")
    public ResponseEntity<List<Compensation>> getAll() {
        return new ResponseEntity<>(compensationService.readAll(), HttpStatus.OK);
    }
}
