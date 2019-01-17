package com.kiwiplan;


import com.kiwiplan.constant.OrganizationConstants;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;


/**
 * This class is responsible for printing organisation hierarchy for given set
 * set of input data in either csv or txt format
 */

public class OrganizationHierarchy {

    private static Map<Integer, Employee> employees;
    private static Employee rootManager;

    private final static Logger logger = Logger.getLogger(OrganizationHierarchy.class.getName());


    public static void main(String[] args) {
        start();
    }


    private static void start() {

        Scanner scanner = new Scanner(System.in);

        //  Prompt for file location
        System.out.print("Please provide the absolute path of file containing organisation hierarchy or type quit to exit: ");
        String input = scanner.next();


        if (StringUtils.isNotBlank(input) && input.equals("quit")) {
            logger.info("Program shutting down...");

            System.exit(0);
        }

        File file = new File(input);

        if (file.exists() && file.isFile()) {
            // get their input as a String
            try {
                consumeDataToMap(input);
            } catch (IOException | NumberFormatException e) {
                logger.info("Problem processing input data.");
                e.printStackTrace();
                System.exit(0);
            }
            buildManagementTree(rootManager);
            displayManagementTree();
        } else {
            logger.warning("File doesn't exist. Please try again." + "\n");

            start();
        }
    }

    /**
     *
     * @param path
     * @throws IOException
     * @throws NumberFormatException
     */
    private static void consumeDataToMap(final String path) throws IOException, NumberFormatException {

        employees = new HashMap<>();
        try (FileReader fileReader = new FileReader(path); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            bufferedReader.readLine(); //skipping columns
            String strLine;
            Employee employee = null;

            while ((strLine = bufferedReader.readLine()) != null) {
                String[] values;

                if(strLine.contains(",")){
                    values = strLine.split(",");
                }else{
                    values = strLine.split(" ");
                }

                if (values.length > 1) {
                    if (StringUtils.isNumeric(values[0]) && StringUtils.isNumeric(values[2])) {
                        employee = new Employee(values[0], values[1], values[2]);
                    } else {
                        throw new NumberFormatException(values[1] + "'s Id or Reporting id is not a number");
                    }
                }
                if (null != employee) {
                    employees.put(employee.getEmployeeId(), employee);
                    if (employee.getReportToId() == 0) {
                        rootManager = employee;
                    }
                }

            }

        }
    }

    /**Build the management tree recursively from a given employee
     *
     * @param employee
     */
    private static void buildManagementTree(Employee employee) {
        List<Employee> juniorEmployees = getJuniorEmployeeById(employee.getEmployeeId());
        employee.setJuniorEmployees(juniorEmployees);
        if (juniorEmployees.size() == 0) {
            return;
        }

        for (Employee e : juniorEmployees) {
            buildManagementTree(e);
        }
    }

    /**Get all the employees reporting to current employee
     *
     * @param reportingID
     * @return Junior Employee List
     */
    private static List<Employee> getJuniorEmployeeById(int reportingID) {
        List<Employee> juniorEmployees = new ArrayList<>();
        for (Employee e : employees.values()) {
            if (e.getReportToId() == reportingID) {
                juniorEmployees.add(e);
            }
        }
        return juniorEmployees;
    }


    /**
     * Iterate recursively on the management tree to display all employees
     *
     *
     */
    private static void displayManagementTree() {
        System.out.print("Here is the management tree: "+ "\n");
        System.out.print("==========================================="+ "\n");
        displayCurrentManagementLevel(rootManager, OrganizationConstants.ROOT_LEVEL);
        System.out.print("===========================================");

    }


    /**Iterate recursively on the management tree to display all employees
     *
     * @param employee
     * @param level
     */
    private static void displayCurrentManagementLevel(Employee employee, int level){
        System.out.print(OrganizationConstants.HIERARCHY_SYMBOL);

        for (int index = 0; index < level; index++) {
            System.out.print(OrganizationConstants.HIERARCHY_SYMBOL);
        }
        System.out.println(employee.getName());

        List<Employee> juniorEmployees = employee.getJuniorEmployees();
        for (Employee e : juniorEmployees) {
            displayCurrentManagementLevel(e, level + 1);
        }
    }
}
