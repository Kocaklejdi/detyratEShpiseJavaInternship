package com.lhind.internship.jdbc.repository;

import com.lhind.internship.jdbc.mapper.EmployeeMapper;
import com.lhind.internship.jdbc.model.Employee;
import com.lhind.internship.jdbc.repository.enums.EmployeeQuery;
import com.lhind.internship.jdbc.util.JdbcConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepository implements Repository<Employee, Integer> {



    private final EmployeeMapper employeeMapper = EmployeeMapper.getInstance();

    @Override
    public List<Employee> findAll() {
        final List<Employee> response = new ArrayList<>();
        try (final Connection connection = JdbcConnection.connect();
             final PreparedStatement statement = connection.prepareStatement(EmployeeQuery.SELECT_ALL.getQuery())) {
            final ResultSet result = statement.executeQuery();
            while (result.next()) {
                response.add(employeeMapper.toEntity(result));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return response;
    }

    @Override
    public Optional<Employee> findById(final Integer id) {
        try (final Connection connection = JdbcConnection.connect();
             final PreparedStatement statement = connection.prepareStatement(EmployeeQuery.SELECT_BY_ID.getQuery())) {
            statement.setInt(1, id);

            final ResultSet result = statement.executeQuery();

            if (result.next()) {
                final Employee employee = employeeMapper.toEntity(result);
                return Optional.of(employee);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean exists(final Integer integer) {
        // TODO: Implement a method which checks if an employee with the given id exists in the employees table
        try(
            final Connection connection = JdbcConnection.connect();
            final PreparedStatement statement = connection.prepareStatement(EmployeeQuery.SELECT_BY_ID.getQuery());){
            statement.setInt(1, integer);
            final ResultSet result = statement.executeQuery();
            if (result.next()) {
                return true;
            }
            return false;
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Employee save(final Employee employee) {

        // TODO: Implement a method which adds an employee to the employees table
        // If the employee exists then the method should instead update the employee
        if (!exists(employee.getEmployeeNumber())){
            try(
                final Connection connection = JdbcConnection.connect();
                final PreparedStatement statement = connection.prepareStatement(EmployeeQuery.INSERT_NEW_LISTING.getQuery());){
                statement.setInt(1,employee.getEmployeeNumber());
                statement.setString(2,employee.getLastName());
                statement.setString(3,employee.getFirstName());
                statement.setString(4,employee.getExtension());
                statement.setString(5,employee.getEmail());
                statement.setString(6,employee.getOfficeCode());
                statement.setInt(7,employee.getReportsTo());
                statement.setString(8,employee.getJobTitle());

                statement.executeUpdate();

            }catch(Exception e){
                System.err.println(e.getMessage());
            }
        } else {
            try (final Connection connection = JdbcConnection.connect(); final PreparedStatement statement = connection.prepareStatement(EmployeeQuery.UPDATE_EXISTING_LISTING.getQuery())) {
                statement.setInt(1,employee.getEmployeeNumber());
                statement.setString(2,employee.getLastName());
                statement.setString(3,employee.getFirstName());
                statement.setString(4,employee.getExtension());
                statement.setString(5,employee.getEmail());
                statement.setString(6,employee.getOfficeCode());
                statement.setInt(7,employee.getReportsTo());
                statement.setString(8,employee.getJobTitle());
                statement.setInt(9,employee.getEmployeeNumber());

                statement.executeUpdate();
            }catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        return null;
    }

    @Override
    public void delete(final Integer integer) {
        /*
         * TODO: Implement a method which deletes an employee given the id
         */

        if (exists(integer)){
            try (final Connection connection = JdbcConnection.connect();
                final PreparedStatement statement = connection.prepareStatement(EmployeeQuery.DELETE_EXISTING_LISTING.getQuery());){
                statement.setInt(1,integer);
                statement.executeUpdate();
            }catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
