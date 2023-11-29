package com.mindex.challenge.service;

import com.mindex.challenge.data.ReportingStructure;

import java.util.Optional;

public interface ReportingStructureService {

    Optional<ReportingStructure> generateReport(String employeeId);

}
