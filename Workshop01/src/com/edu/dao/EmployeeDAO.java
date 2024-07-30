package com.edu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.edu.vo.Employee;

public interface EmployeeDAO {
	Connection getConnect();
	void closeAll(PreparedStatement ps, Connection conn);
	void closeAll(ResultSet rs, PreparedStatement ps, Connection conn);
	void insertEmployee(Employee emp);
	void removeEmployee (int num);
	void updateEmployee (Employee emp);
	ArrayList<Employee> selectEmployee ();
	ArrayList<Employee> selectEmployee (int num);
}
