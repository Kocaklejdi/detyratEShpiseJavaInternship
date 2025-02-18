package com.lhind.internship.jdbc.main;

import com.lhind.internship.jdbc.model.Employee;
import com.lhind.internship.jdbc.repository.EmployeeRepository;

public class Main {

    public static void main(String[] args) {
        EmployeeRepository employeeRepository =new EmployeeRepository();

        Employee employee = new Employee(1010,
                "kocaa",
                "klejdi",
                "x300",
                "kocaklejdi@gmail.com",
                "1",
                1002,
                "Sales");

        employeeRepository.save(employee);

        employeeRepository.findAll().forEach(System.out::println);

        employeeRepository.delete(1010);
        employeeRepository.findAll().forEach(System.out::println);
    }
}
