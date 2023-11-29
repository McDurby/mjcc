package com.mindex.challenge.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    private String postCompensationUrl;
    private String getCompensationUrl;
    private String getAllCompensationsUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        postCompensationUrl = "http://localhost:" + port + "/employee/{id}/compensation";
        getCompensationUrl = "http://localhost:" + port + "/employee/{id}/compensation";
        getAllCompensationsUrl = "http://localhost:" + port + "/employee/compensations";
    }

    @Test
    public void createAndGetCompensation() {
        String employeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
        String dateString = "12/01/2023 13:00:30";
        float salary = 123.00f;

        Compensation createCompensation = new Compensation();
        createCompensation.setSalary(salary);
        createCompensation.setEffectiveDate(toDate(dateString));

        Compensation compensation = restTemplate.postForEntity(postCompensationUrl, createCompensation, Compensation.class, employeeId).getBody();
        assertNotNull("compensation is null for " + employeeId, compensation);
        Employee employee = compensation.getEmployee();
        employee = compensation.getEmployee();
        assertEquals(employeeId, employee.getEmployeeId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Lennon", employee.getLastName());
        assertEquals(salary, compensation.getSalary(), 0);
        assertEquals(dateString, parseDate(compensation.getEffectiveDate()));

        compensation = restTemplate.getForEntity(getCompensationUrl, Compensation.class, employeeId).getBody();
        employee = compensation.getEmployee();
        assertNotNull("employee null for id: " + employeeId, employee);
        assertEquals(employeeId, employee.getEmployeeId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Lennon", employee.getLastName());
        assertEquals(salary, compensation.getSalary(), 0);
        assertEquals(dateString, parseDate(compensation.getEffectiveDate()));

        // create a second compensation to test get all
        employeeId = "b7839309-3348-463b-a7e3-5de1c168beb3";
        dateString = "12/02/2023 18:00:30";
        salary = 250.00f;

        createCompensation = new Compensation();
        createCompensation.setSalary(salary);
        createCompensation.setEffectiveDate(toDate(dateString));

        restTemplate.postForEntity(postCompensationUrl, createCompensation, Compensation.class, employeeId);

        // get all to test size
        ResponseEntity<Object[]> response = restTemplate.getForEntity(getAllCompensationsUrl, Object[].class);
        Object[] objects = response.getBody();
        assertNotNull("compensation list is null", objects);
        ObjectMapper mapper = new ObjectMapper();
        List<Compensation> list = Arrays.stream(objects)
                .map(object -> mapper.convertValue(object, Compensation.class))
                .collect(Collectors.toList());
        assertEquals(2, list.size());
    }

    @Test
    public void createBadCompensation() {
        String employeeId = "1";
        String dateString = "12/01/2023 13:00:30";
        float salary = 123.00f;

        Compensation createCompensation = new Compensation();
        createCompensation.setSalary(salary);
        createCompensation.setEffectiveDate(toDate(dateString));

        Compensation compensation = restTemplate.postForEntity(postCompensationUrl, createCompensation, Compensation.class, employeeId).getBody();
        assertNull("compensation is not null for " + employeeId, compensation);
    }

    @Test
    public void getMissingCompensation() {
        String employeeId = "1";
        Compensation compensation = restTemplate.getForEntity(getCompensationUrl, Compensation.class, employeeId).getBody();
        assertNull("compensation is not null for " + employeeId, compensation);
    }

    private Date toDate(String string) {
        try {
            return dateFormat.parse(string);
        } catch (ParseException ex) {
            ex.printStackTrace(System.err);
            return null;
        }
    }

    private String parseDate(Date date) {
        return dateFormat.format(date);
    }

}
