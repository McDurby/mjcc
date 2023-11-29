package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return new ResponseEntity<>(employeeService.create(employee), HttpStatus.OK);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        try {
            return new ResponseEntity<>(employeeService.read(id), HttpStatus.OK);
        } catch (RuntimeException ex) {
            LOG.debug("Employee not found id [{}]", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee update request for id [{}] and employee [{}]", id, employee);

        try {
            Employee updateEmployee = employeeService.read(id);

            // this could duplicate the entries for an employee
            if (employee.getFirstName() != null) {
                updateEmployee.setFirstName(employee.getFirstName());
            }
            if (employee.getLastName() != null) {
                updateEmployee.setLastName(employee.getLastName());
            }
            if (employee.getPosition() != null) {
                updateEmployee.setPosition(employee.getPosition());
            }
            if (employee.getDepartment() != null) {
                updateEmployee.setDepartment(employee.getDepartment());
            }
            if (employee.getDirectReports() != null) {
                updateEmployee.setDirectReports(employee.getDirectReports());
            }

            employeeService.update(updateEmployee);
            return ResponseEntity.ok(updateEmployee);
            //return employeeService.update(employee);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
