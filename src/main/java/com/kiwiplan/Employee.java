package com.kiwiplan;


import java.util.List;

/**
 * This class is responsible for holding the information about employee
 */

public class Employee {

    private String name;

    private int empId;

    private int reportToId;

    private List<Employee> juniorEmployees;

    public List<Employee> getJuniorEmployees() {
        return juniorEmployees;
    }

    public void setJuniorEmployees(List<Employee> juniorEmployees) {
        this.juniorEmployees = juniorEmployees;
    }

    public int getEmployeeId() {
        return empId;
    }

    public void setEmployeeId(int id) {
        this.empId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public int getReportToId() {
        return reportToId;
    }

    /**
     *
     * @param id
     * @param empName
     * @param reportingId
     */
    public Employee(String id, String empName, String reportingId) {

        this.empId = Integer.parseInt(id);
        this.name = empName;
        this.reportToId = Integer.parseInt(reportingId);

    }


}

