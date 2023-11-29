package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String generateReportUrl;

    @Autowired
    private ReportingStructureService reportingService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        generateReportUrl = "http://localhost:" + port + "/employee/{id}/generate-report";
    }

    @Test
    public void testGenerateReport1() {
        String employeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
        ReportingStructure report = restTemplate.getForEntity(generateReportUrl, ReportingStructure.class, employeeId).getBody();
        assertNotNull("report is null", report);

        Employee employee = report.getEmployee();
        assertNotNull(employee.getEmployeeId());
        assertEquals(employeeId, employee.getEmployeeId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Lennon", employee.getLastName());
        assertEquals(4, report.getNumberOfReports());
    }

    @Test
    public void testGenerateReport2() {
        String employeeId = "b7839309-3348-463b-a7e3-5de1c168beb3";
        ReportingStructure report = restTemplate.getForEntity(generateReportUrl, ReportingStructure.class, employeeId)
                .getBody();
        assertNotNull("report is null", report);

        Employee employee = report.getEmployee();
        assertNotNull(employee.getEmployeeId());
        assertEquals(employeeId, employee.getEmployeeId());
        assertEquals("Paul", employee.getFirstName());
        assertEquals("McCartney", employee.getLastName());
        assertEquals(0, report.getNumberOfReports());
    }

    @Test
    public void testFailedGenerateReport() {
        String employeeId = "1";
        ReportingStructure report = restTemplate.getForEntity(generateReportUrl, ReportingStructure.class, employeeId).getBody();
        assertNull("report not null for invalid id " + employeeId, report);
    }

}
