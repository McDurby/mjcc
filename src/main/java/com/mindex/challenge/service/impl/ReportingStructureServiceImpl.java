package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Optional<ReportingStructure> generateReport(String employeeId) {
        LOG.debug("Generate reporting structure for employee ID [{}]", employeeId);

        // Assumption:
        // * each only employee only reports to one "manager"

        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee != null) {
            int numberOfReports = reporting(employee);
            return Optional.of(new ReportingStructure(employee, numberOfReports));
        }

        LOG.debug("Failed to find employee with ID [{}]", employeeId);
        return Optional.empty();
    }

    private int reporting(Employee employee) {
        int count = 0;

        List<Employee> directReports = employee.getDirectReports();
        if (directReports != null && !directReports.isEmpty()) {
            count = directReports.size();
            for (Employee directReport : directReports) {
                // NOTE: directReport only has the direct.employeeId, must retrieve the Employee instance
                count += reporting(employeeRepository.findByEmployeeId(directReport.getEmployeeId()));
            }
        }

        return count;
    }

}
