package com.edu.jdbc.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBC4ProcessTest2 {
	public static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
	public static final String URL = "jdbc:mysql://127.0.0.1:3306/kosta?serverTimezone=UTC&useUnicode=yes&characterEncoding=UTF-8";
	public static final String USER = "root";
	public static final String PASSWORD = "1234";
	
	public JDBC4ProcessTest2(){
		try {
			//2. DB서버 연결
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("2. DB연결 성공");
			
			//INSERT
			/*
			//3.
			String query = "INSERT INTO custom (id, name, address) VALUES(?, ? ,?)";
			PreparedStatement ps=conn.prepareStatement(query); 
			System.out.println("3. PreparedStatement 생성");

			
			//4. 바인딩 및 쿼리문 실행
			ps.setInt(1, 777);
			ps.setString(2, "황선우");
			ps.setString(3, "서울");
			
			System.out.println("4. "+ps.executeUpdate() + " ROW Record 등록 성공");
			*/
			
			//DELETE...id가 222인 사람 삭제
			/*
			String query = "DELETE FROM custom WHERE id=?";
			
			PreparedStatement ps = conn.prepareStatement(query);
			System.out.println("3. PreparedStatement 생성");
			ps.setInt(1, 222);
			
			System.out.println("4. "+ps.executeUpdate() + " ROW Record 삭제 성공");
			 */
			
			//UPDATE...id값이 444인 사람의 정보를 수정...name(오상욱), address(광주)
			/*
			String query = "UPDATE custom SET name=?, address=? WHERE id=?";
			
			PreparedStatement ps = conn.prepareStatement(query);
			System.out.println("3. PreparedStatement 생성");
			ps.setString(1, "오상욱");
			ps.setString(2, "광주");
			ps.setInt(3, 444);
			
			System.out.println("4. "+ps.executeUpdate() + " ROW Record 수정 성공");
			*/
			
			//조회하기
			/*
			String query = "SELECT id, name, address FROM custom";
			PreparedStatement ps = conn.prepareStatement(query);
			
			ResultSet rs=ps.executeQuery();
			System.out.println("-------------------------------------");
			while(rs.next()) {
				System.out.println(rs.getInt("id")+", "
									+rs.getString("name")+", "
									+rs.getString("address"));
			}
			System.out.println("-------------------------------------");
			*/
			
		}catch(SQLException e ){
			System.out.println("2. DB연결 실패");
		}
	}//JDBC4ProcessTest2 생성자
	
	public static void main(String[] args) {
		new JDBC4ProcessTest2();
	}//main
	
	static {
		//1. 드라이버 로딩
		//[DB연결 전에 드라이버 로딩이 반드시 선행되어야 하기 때문에, 보다 글로벌한 초기화가 이뤄지는 static{}에서 드라이버 로딩 진행]
		try {
			Class.forName(DRIVER_NAME);
			System.out.println("1. 드라이버 로딩 성공");
		}catch(ClassNotFoundException e){
			System.out.println("1. 드라이버 로딩 실패");
		}
	}//static
}//JDBC4ProcessTest2
