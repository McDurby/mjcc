package com.mindex.challenge.controller;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ReportingStructureController {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureController.class);

    @Autowired
    private ReportingStructureService reportingStructureService;

    @GetMapping("/employee/{id}/generate-report")
    public ResponseEntity<ReportingStructure> generateReport(@PathVariable String id) {
        LOG.debug("Generate employee report for [{}]", id);

        Optional<ReportingStructure> reportingStructure = reportingStructureService.generateReport(id);
        if (reportingStructure.isPresent()) {
            return new ResponseEntity<>(reportingStructure.get(), HttpStatus.OK);
        }
        LOG.debug("Failed to find employee with ID [{}]", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // TODO: functional technique...team standards?
        /*
        Optional<ReportingStructure> reportingStructure = reportingStructureService.generateReport(id);
        return reportingStructure.map(structure -> new ResponseEntity<>(structure, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        */
    }

}
