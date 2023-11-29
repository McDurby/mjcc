package com.mindex.challenge.data;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Compensation {

    @Id
    private String employeeId;

    private Employee employee;

    private float salary;

    private Date effectiveDate;

    public Compensation() {

    }

    public Compensation(Employee employee, float salary, Date effectiveDate) {
        this.employee = employee;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
