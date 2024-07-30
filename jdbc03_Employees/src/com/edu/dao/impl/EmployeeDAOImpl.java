package com.edu.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	
	private boolean isExist(int num, Connection conn) throws SQLException{ // PK에 해당하는 사람이 있는지 여부를 리턴한다.
		PreparedStatement ps =null;
		ResultSet rs = null;
		
		String query = "select num from employee where num =?";
		
		ps = conn.prepareStatement(query);
		
		ps.setInt(1, num);
		rs = ps.executeQuery();
		
		return rs.next(); // 있으면 true, 없으면 false
	}
	
	
	public void insertEmployee(Employee emp) throws SQLException, DuplicateNumException {
		Connection conn = null;
		PreparedStatement ps =null;
		
		try {
			conn = getConnection();
			
			//isExist() 함수를 호출해서 사람이 없으면 아래 코드가 실행되도록 로직을 제어한다.
			if(!isExist(emp.getNum(), conn) ) {
				String query = "INSERT INTO employee(num, name, salary, address) VALUES(?,?,?,?)";
				ps = conn.prepareStatement(query);
				
				ps.setInt(1, emp.getNum());
				ps.setString(2, emp.getName());
				ps.setDouble(3, emp.getSalary());
				ps.setString(4, emp.getAddress());
		
				System.out.println(ps.executeUpdate() + " employee 등록 성공");
			}
			else {
				throw new DuplicateNumException(emp.getName() + "님은 이미 회원입니다.");
			}
			
		}finally {

			closeAll(ps, conn); //무조건 자원 반환은 된다.
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
