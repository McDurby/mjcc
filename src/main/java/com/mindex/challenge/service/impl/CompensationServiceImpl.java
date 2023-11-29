package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Optional<Compensation> create(String employeeId, Compensation compensation) {
        LOG.debug("Create compensation for employee with id [{}] and compensation [{}]", employeeId, compensation);
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee != null) {
            compensation.setEmployeeId(employeeId);
            compensation.setEmployee(employee);
            return Optional.of(compensationRepository.save(compensation));
        }

        LOG.debug("Failed to find employee with ID [{}]", employeeId);
        return Optional.empty();
    }

    public Optional<Compensation> read(String employeeId) {
        LOG.debug("Reading compensation for employee with id [{}]", employeeId);

        return compensationRepository.findById(employeeId);
    }

    public List<Compensation> readAll() {
        return compensationRepository.findAll();
    }

}
