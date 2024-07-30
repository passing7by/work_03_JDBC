package com.edu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.edu.vo.Custom;

import config.ServerInfo;

public class SimpleJDBCTest {
	public SimpleJDBCTest() throws Exception{
		//1. 드라이버 로딩
		Class.forName(ServerInfo.DRIVER_NAME);
		System.out.println("1. 드라이버 로딩 성공");
	}//SimpleJDBCTest()
	
	//고정적으로 각 메소드마다 반복되는 로직을 공통로직으로 정의...Connection 반환, clse()
	public Connection getConnect()throws SQLException{
		Connection conn = DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASSWORD);
		System.out.println("2. DB 연결 성공");
		return conn;
	}
	
	//열린 순서 반대로 닫아준다
	public void closeAll(PreparedStatement ps, Connection conn) throws Exception{
		if(ps !=null) ps.close();
		if(conn !=null) conn.close();
	}
	//메소드 오버로딩
	public void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws Exception{
		if(rs !=null) rs.close();
		closeAll(ps, conn);
	}
	
	public void addCustom(Custom c) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		
		//insert into
		//1. 디비연결
		conn = getConnect();
		//2. PreparedStatement 생성
		String query = "INSERT INTO custom (id, name, address) VALUES(?, ? ,?)";
		ps=conn.prepareStatement(query);
		System.out.println("3. PreparedStatement 생성");
		//3. 바인딩
		ps.setInt(1, c.getId());
		ps.setString(2, c.getName());
		ps.setString(3, c.getAddress());
		//4. 쿼리문 실행
		System.out.println("4. "+ps.executeUpdate() + " ROW Record 등록 성공");
		//5. 자원 반환 (중요)
		closeAll(ps, conn);
	}//addCustom()
	
	
	public Custom getCustom(int id) throws Exception {
		Custom custom = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//select where
		//1. 디비연결
		conn = DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASSWORD);
		System.out.println("2. DB 연결 성공");
		//2. PreparedStatement 생성
		String query = "SELECT id, name, address FROM custom WHERE id=?";
		ps = conn.prepareStatement(query);
		//3. 바인딩
		ps.setInt(1, id);
		//4. 쿼리문 실행
		rs=ps.executeQuery();
		
		if(rs.next()) 
			custom = new Custom(id,
								rs.getString("name"),
								rs.getString("address"));
		//5. 자원 반환 (중요)
		closeAll(rs, ps, conn);
		
		return custom;
	}//getCustom()
	
	public ArrayList<Custom> getCustom() throws Exception {
		ArrayList<Custom> list = new ArrayList<Custom>();
		//select where
		//1. 디비연결
		Connection conn = DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASSWORD);
		System.out.println("2. DB 연결 성공");
		//2. PreparedStatement 생성
		String query = "SELECT id, name, address FROM custom";
		PreparedStatement ps = conn.prepareStatement(query);
		//3. 바인딩
		
		//4. 쿼리문 실행
		ResultSet rs=ps.executeQuery();
		
		while(rs.next()) {
			list.add(new Custom(rs.getInt("id"),
								rs.getString("name"),
								rs.getString("address")));
		}
		//5. 자원 반환 (중요)
		conn.close();
		
		return list;
	}//
	
	public static void main(String[] args) throws Exception{
		SimpleJDBCTest dao = new SimpleJDBCTest();
		dao.addCustom(new Custom(555, "Blake", "LA"));
		Custom c = dao.getCustom(111);
		System.out.println(c);
		
		dao.getCustom().stream()
						.forEach(i->System.out.println(i));
	}//main

}//SimpleJDBCTest
