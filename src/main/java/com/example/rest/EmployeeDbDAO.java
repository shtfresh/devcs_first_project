/* Copyright © 2016 Oracle and/or its affiliates. All rights reserved. */

package com.example.rest;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class EmployeeDbDAO implements EmployeeDAO{
    
    List<Employee> eList = null;
	private final Connection conn = DBConnection.getInstance().getConnection();

	    
	public List<Employee> query(String sqlQueryStr) {
		List<Employee> resultList = new ArrayList<>();
		try (PreparedStatement stmt = conn.prepareStatement(sqlQueryStr)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				resultList.add(
                    new Employee(rs.getLong("ID"), rs.getString("FIRSTNAME"), 
                        rs.getString("LASTNAME"), rs.getString("EMAIL"), rs.getString("PHONE"), 
                        rs.getString("BIRTHDATE"), rs.getString("TITLE"), rs.getString("DEPARTMENT"))
                );
			}
		} catch (SQLException e) {
            System.out.println("SQL Query Error: " + e.getMessage());
		} catch (Exception e) {
            System.out.println("Query Error: " + e.getMessage());
		}
		return resultList;
	}

    @Override
    public List<Employee> getAllEmployees(){
		String queryStr = "SELECT * FROM EMPLOYEE";
		List<Employee> resultList = this.query(queryStr);
        return resultList;	
    }
   

    @Override
    public Employee getEmployee(long id){
		String queryStr = "SELECT * FROM EMPLOYEE WHERE ID=" + id;
		List<Employee> resultList = this.query(queryStr);
                
		if (resultList.size() > 0) {
			return resultList.get(0);
		} else {
            return null;
        }    
    }
    

    @Override
    public List<Employee> getByLastName(String name){
		String queryStr = "SELECT * FROM EMPLOYEE WHERE LASTNAME LIKE '" + name + "%'";
		List<Employee> resultList = this.query(queryStr);
         
        return resultList;
    }
    
    
    @Override
    public List<Employee> getByTitle(String title){
		String queryStr = "SELECT * FROM EMPLOYEE WHERE TITLE LIKE '" + title + "%'";
		List<Employee> resultList = this.query(queryStr);
         
        return resultList;
    }

    
    @Override
    public List<Employee> getByDepartment(String department){
		String queryStr = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT LIKE'" + department + "%'";
		List<Employee> resultList = this.query(queryStr);
         
        return resultList;
    }
    
    
    @Override
    public boolean add(Employee employee){
		String insertTableSQL = "INSERT INTO EMPLOYEE "
				+ "(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE, BIRTHDATE, TITLE, DEPARTMENT) "
				+ "VALUES(EMPLOYEE_SEQ.NEXTVAL,?,?,?,?,?,?,?)";

		try (PreparedStatement preparedStatement = this.conn
				.prepareStatement(insertTableSQL)) {

			preparedStatement.setString(1, employee.getFirstName());
			preparedStatement.setString(2, employee.getLastName());
			preparedStatement.setString(3, employee.getEmail());
			preparedStatement.setString(4, employee.getPhone());
			preparedStatement.setString(5, employee.getBirthDate());
			preparedStatement.setString(6, employee.getTitle());
			preparedStatement.setString(7, employee.getDepartment());

			preparedStatement.executeUpdate();
            return true;
		} catch (SQLException e) {
            System.out.println("SQL Add Error: " + e.getMessage());
            return false;
            
		} catch (Exception e) {
            System.out.println("Add Error: " + e.getMessage());
            return false;
		}
        
    }
    
    
    @Override
    public boolean update(long id, Employee employee){
		String updateTableSQL = "UPDATE EMPLOYEE SET FIRSTNAME=?, LASTNAME=?, EMAIL=?, PHONE=?, BIRTHDATE=?, TITLE=?, DEPARTMENT=?  WHERE ID=?";
		try (PreparedStatement preparedStatement = this.conn
				.prepareStatement(updateTableSQL);) {
			preparedStatement.setString(1, employee.getFirstName());
			preparedStatement.setString(2, employee.getLastName());
			preparedStatement.setString(3, employee.getEmail());
			preparedStatement.setString(4, employee.getPhone());
			preparedStatement.setString(5, employee.getBirthDate());
			preparedStatement.setString(6, employee.getTitle());
			preparedStatement.setString(7, employee.getDepartment());
            preparedStatement.setLong(8, employee.getId());

			preparedStatement.executeUpdate();
            return true;
		} catch (SQLException e) {
            System.out.println("SQL Update Error: "	+ e.getMessage());
            return false;            
		} catch (Exception e) {
            System.out.println("Update Error: "	+ e.getMessage());
            return false;            
		}
    
    }

    
    @Override
    public boolean delete(long id){
		String deleteRowSQL = "DELETE FROM EMPLOYEE WHERE ID=?";
		try (PreparedStatement preparedStatement = this.conn
				.prepareStatement(deleteRowSQL)) {
			preparedStatement.setLong(1, id);
			preparedStatement.executeUpdate();
            return true;

		} catch (SQLException e) {
			System.out.println("SQL Delete Error: " + e.getMessage());
            return false;
		} catch (Exception e) {
			System.out.println("Delete Error: " + e.getMessage());
            return false;
		}
	}
}
