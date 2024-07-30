package com.edu.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import com.edu.exception.DuplicateNumException;
import com.edu.vo.Employee;

import config.ServerInfo;

public class EmployeeDAOImpl {
	
	//싱글톤 생성
	//DAO 클래스 상속은 생략
	private static EmployeeDAOImpl dao = new EmployeeDAOImpl();
	
	private EmployeeDAOImpl(){ // 생성자가 비어있을 때는 static 으로 따로 빼지 않아도 됨
		try {
			Class.forName(ServerInfo.DRIVER_NAME);
			System.out.println("Driver Loading 성공...");
			
		} catch (ClassNotFoundException e) {
			System.out.println("Driver Loading 실패");
		}
	}

	
	public static EmployeeDAOImpl getInstance() {
		return dao;
	}
	
	
	//////////////////////////////// 공통 로직(고정적인 부분) //////////////////////////////////
	
	public Connection getConnection() throws SQLException{
		Connection conn = DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASSWORD);
		System.out.println("DB Connection 성공");
		return conn;
	}
	
	public void closeAll(PreparedStatement ps, Connection conn) throws SQLException {
		if( ps != null) ps.close();
		if( conn != null) conn.close();
	}
	
	public void closeAll(ResultSet rs, PreparedStatement ps, Connection conn ) throws SQLException {
		if( rs != null) rs.close();
		closeAll(ps, conn);
		
	}
	
	//////////////////////////////// 비즈니스 로직(가변적인 부분) //////////////////////////////////
	
	//isExist 사용 안함 -> 쿼리문을 두 번 돌릴 필요가 없다.(select 를 안돌림. 성능이 더 좋다)
	public void insertEmployee(Employee emp) {
		String query = "INSERT INTO employee(num, name, salary, address) VALUES(?,?,?)";
		
		try(Connection conn = getConnection(); 
			PreparedStatement ps = conn.prepareStatement(query)) {
				
			ps.setInt(1, emp.getNum());
			ps.setString(2, emp.getName());
			ps.setDouble(3, emp.getSalary());
			ps.setString(4, emp.getAddress());
	
			System.out.println(ps.executeUpdate() + " employee 등록 성공");
			
		}catch (SQLIntegrityConstraintViolationException e) { // 중복 오류 (isExist를 만들지 않아도 되고, connection 이 두번되지 않는다.)
			System.out.println(e.getMessage());
			
		}catch (SQLException e) {	//sql 문법 오류
			System.out.println(e.getMessage());
			
		}
	}
	
	
	public void removeEmployee(int num) throws SQLException {
		Connection conn = null;
		PreparedStatement ps =null;
		
		conn = getConnection();
		
		String query = "delete from employee where num =?";
		ps = conn.prepareStatement(query);
		
		ps.setInt(1, num);
		
		System.out.println(ps.executeUpdate() + " employee 삭제 성공");
		
		closeAll(ps, conn);
	}

	public void updateEmployee(Employee emp) throws SQLException {
		Connection conn = null;
		PreparedStatement ps =null;
		
		conn = getConnection();
		
		String query = "UPDATE employee SET name=?, salary=?, address=? WHERE num=?";
		ps = conn.prepareStatement(query);
		
		ps.setString(1, emp.getName());
		ps.setDouble(2, emp.getSalary());
		ps.setString(3, emp.getAddress());
		ps.setInt(4, emp.getNum());
		
		ps.executeUpdate();
		
		closeAll(ps, conn);

	}
	
	public ArrayList<Employee> selectEmployee() throws SQLException {
		ArrayList<Employee> list = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement ps = null;
			
		conn = getConnection();
		
		String query = "select num, name, salary, address from employee";
		ps = conn.prepareStatement(query);
		
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			list.add( new Employee(	rs.getInt("num"),
								 	rs.getString("name"),
								 	rs.getDouble("salary"),
								 	rs.getString("address")) );
		}
		
		closeAll(rs, ps, conn);
		
		return list;
	}
	
	
	public Employee selectEmployee(int num) throws SQLException {
		Employee e = null;
		Connection conn = null;
		PreparedStatement ps = null;
		
		conn = getConnection();
		
		String query = "select num, name, salary, address from employee where num =?";
		ps = conn.prepareStatement(query);
		
		ps.setInt(1, num);
		
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			e = new Employee(	rs.getInt("num"),
							 	rs.getString("name"),
							 	rs.getDouble("salary"),
							 	rs.getString("address"));
		}
		
		closeAll(rs, ps, conn);
		
		return e;
	}
	
}
