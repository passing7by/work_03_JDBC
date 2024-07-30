package com.edu.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.edu.exception.DMLException;
import com.edu.exception.DuplicateNumException;
import com.edu.exception.RecordNotFoundException;
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
	public void insertEmployee(Employee emp) throws DMLException, DuplicateNumException {
		String query = "INSERT INTO employee(num, name, salary, address) VALUES(?,?,?,?)";
		
		try(Connection conn = getConnection(); 
			PreparedStatement ps = conn.prepareStatement(query)) {
				
			ps.setInt(1, emp.getNum());
			ps.setString(2, emp.getName());
			ps.setDouble(3, emp.getSalary());
			ps.setString(4, emp.getAddress());
	
			System.out.println(ps.executeUpdate() + " employee 등록 성공");
			
		}catch (SQLIntegrityConstraintViolationException e) { // 중복 오류 (isExist를 만들지 않아도 되고, connection 이 두번되지 않는다.)
			throw new DuplicateNumException("이미 회원가입된 상태입니다. 다시 확인해주세요");
			
		}catch (SQLException e) {	//sql 문법 오류
			throw new DMLException("회원 가입 시 문제가 발생해 가입이 이뤄지지 않았습니다.");
			
		}
	}
	
	
	public void removeEmployee(int num) throws DMLException, RecordNotFoundException {
		
		String query = "delete from employee where num =?";
		
		try(Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query))  {
			
			ps.setInt(1, num);
			
			if(ps.executeUpdate() ==0) {
				throw new RecordNotFoundException("존재하지 않는 대상입니다.");
			}
				
		}catch (SQLException e) {	//sql 문법 오류
			throw new DMLException("회원 삭제 시 문제가 발생해 가입이 이뤄지지 않았습니다.");
		}
	
	}
	
	
	public void updateEmployee(Employee emp) throws RecordNotFoundException, DMLException {
		
		String query = "UPDATE employee SET name=?, salary=?, address=? WHERE num=?";
		try(Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query))  {
				
			ps.setString(1, emp.getName());
			ps.setDouble(2, emp.getSalary());
			ps.setString(3, emp.getAddress());
			ps.setInt(4, emp.getNum());
				
			if(ps.executeUpdate() ==0) {
				throw new RecordNotFoundException("존재하지 않는 대상입니다.");
			}
				
		}catch (SQLException e) {	//sql 문법 오류
			throw new DMLException("회원 삭제 시 문제가 발생해 가입이 이뤄지지 않았습니다.");
		}
	
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
