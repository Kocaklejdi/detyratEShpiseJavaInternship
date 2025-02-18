package com.lhind.internship.jdbc.repository.enums;

public enum EmployeeQuery {

    SELECT_ALL("SELECT * FROM employees;"),
    SELECT_BY_ID("SELECT * FROM employees WHERE employeeNumber = ?;"),
    DELETE_BY_ID("DELETE FROM employees WHERE employeeNumber = ?;"),
    INSERT_NEW_LISTING("INSERT INTO employees VALUES (?, ?, ?, ?, ?, ?, ?, ?);"),
    UPDATE_EXISTING_LISTING("UPDATE employees SET employeeNumber = ?, lastName = ?, firstName = ?, extension = ?, email = ?, officeCode = ?, reportsTo = ?, jobTitle = ? WHERE employeeNumber = ?"),
    DELETE_EXISTING_LISTING("DELETE FROM employees WHERE employeeNumber = ?");

    private final String query;

    EmployeeQuery(final String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
