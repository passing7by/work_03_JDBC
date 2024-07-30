package com.edu.dao.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import com.edu.vo.Employee;

import config.ServerInfo;

public class EmployeeDAOImpl {
	
	static String driver = ServerInfo.DRIVER_NAME;
	static String url =ServerInfo.URL;
	static String user = ServerInfo.USER;
	static String pass = ServerInfo.PASSWORD;
	
	//싱글톤
	private ArrayList<Employee> list;
	
	static private EmployeeDAOImpl service = new EmployeeDAOImpl();
	private EmployeeDAOImpl() {
		list = new ArrayList<Employee>();
	}
	public static EmployeeDAOImpl getInstance() {
		return service;
	}
	
	
	//1. 커넥션 객체 반환 (DB서버 연결)
	public Connection getConnect() throws SQLException{
		Connection conn = DriverManager.getConnection(url, user, pass);
		System.out.println("2. DB 연결 성공");
		return conn;
	}
	//5. 자원 반환
	public void closeAll(PreparedStatement ps, Connection conn) throws SQLException{
		if(ps != null) ps.close();
		if(conn != null) conn.close();
		System.out.println("close");
	}
	//메소드 오버로딩
	public void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException{
		if(rs != null) rs.close();
		closeAll(ps, conn);
		System.out.println("close");
	}
	
	public void insertEmployee(Employee emp) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		//1. 디비 연결
		conn = getConnect();
		//2. PreparedStatement 생성
		String query = "INSERT INTO employee (num, name, salary, address) VALUES(?, ?, ?, ?)";
		ps = conn.prepareStatement(query);
		System.out.println("3. PreparedStatement 생성");
		//3. 바인딩
		ps.setInt(1, emp.getNum());
		ps.setString(2, emp.getName());
		ps.setDouble(3, emp.getSalary());
		ps.setString(4, emp.getAddress());
		//4. 쿼리문 실행
		System.out.println("4. "+ps.executeUpdate() + " ROW Record 추가 성공");
		//5. 자원 반환
		closeAll(ps, conn);
	}
	public void removeEmployee (int num) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		
		//1. 디비 연결
		conn = getConnect();
		//2. PreparedStatement 생성
		String query = "DELETE FROM employee WHERE num=?";
		ps = conn.prepareStatement(query);
		System.out.println("3. PreparedStatement 생성");
		//3. 바인딩
		ps.setInt(1, num);
		//4. 쿼리문 실행
		System.out.println("4. "+ps.executeUpdate() + " ROW Record 삭제 성공");
		//5. 자원 반환
		closeAll(ps, conn);
	}
	public void updateEmployee (Employee emp) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		
		//1. 디비 연결
		conn = getConnect();
		//2. PreparedStatement 생성
		String query = "UPDATE employee SET name=?, salary=?, address=? WHERE num=?";
		ps = conn.prepareStatement(query);
		System.out.println("3. PreparedStatement 생성");
		//3. 바인딩
		ps.setString(1, emp.getName());
		ps.setDouble(2, emp.getSalary());
		ps.setString(3, emp.getAddress());
		ps.setInt(4, emp.getNum());
		//4. 쿼리문 실행
		System.out.println("4. "+ps.executeUpdate() + " ROW Record 수정 성공");
		//5. 자원 반환
		closeAll(ps, conn);
	}
	public ArrayList<Employee> selectEmployee () throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//1. 디비 연결
		conn = getConnect();
		//2. PreparedStatement 생성
		String query = "SELECT * FROM employee";
		ps = conn.prepareStatement(query);
		System.out.println("3. PreparedStatement 생성");
		//3. 바인딩
		
		//4. 쿼리문 실행
		rs=ps.executeQuery();
		
		while(rs.next()) {
			list.add(new Employee(rs.getInt("num"),
								rs.getString("name"),
								rs.getInt("salary"),
								rs.getString("address")));
		}
		//5. 자원 반환 (중요)
		closeAll(rs, ps, conn);

		return list;
	}
	public ArrayList<Employee> selectEmployee (int num) throws SQLException{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//1. 디비 연결
		conn = getConnect();
		//2. PreparedStatement 생성
		String query = "SELECT * FROM employee WHERE num=?";
		ps = conn.prepareStatement(query);
		System.out.println("3. PreparedStatement 생성");
		//3. 바인딩
		ps.setInt(1, num);
		//4. 쿼리문 실행
		rs=ps.executeQuery();
		
		if(rs.next()) {
			list.add(new Employee(num,
					rs.getString("name"),
					rs.getInt("salary"),
					rs.getString("address")));
		}

		//5. 자원 반환 (중요)
		closeAll(rs, ps, conn);

		return list;
	}
	
}



