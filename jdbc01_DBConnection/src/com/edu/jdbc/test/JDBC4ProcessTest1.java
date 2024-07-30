package com.edu.jdbc.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
   JDBC 기본 4단계 닥업
   1. 서버 정보를 담고 있는 드라이버 로딩
   2. DB서버 연결...Connection 객체가 반환되고, 클라이언트로 받으면 됨
   3. PreparedStatement 객체 생성
   4. SQL 쿼리문 실행
 */
public class JDBC4ProcessTest1 {
	public JDBC4ProcessTest1(){
		try {
			//1. 드라이버 로딩
			Class.forName("com.mysql.cj.jdbc.Driver");//드라이버명 입력 - FQCN
			//[Class : 실행 중인 자바 프로그램에서 클래스와 인터페이스를 표현하는(정보를 담는) 클래스]
			//[forName() : 클래스의 이름을 매개변수로 받아서 Class 객체를 리턴]
			System.out.println("1. 드라이버 로딩 성공");
			
			//2. DB서버 연결
			String url = 
				"jdbc:mysql://127.0.0.1:3306/kosta?serverTimezone=UTC&useUnicode=yes&characterEncoding=UTF-8";
				// jdbc:mysql -> 프로토콜 - ':' 뒤에는 벤더 이름이 들어감
				//127.0.0.1 -> localhost IP 주소
				//3306 -> mysql
				//kosta -> database(스키마)
			Connection conn = DriverManager.getConnection(url, "root", "1234");
			//[DriverManager : 데이터 원본에 JDBC 드라이버를 통하여 커넥션을 만드는 역할을 함]
			System.out.println("2. DB연결 성공");
			
			//3. PreparedStatement 객체 생성
			String query = "INSERT INTO custom (id, name, address) VALUES(?,?,?)";
			PreparedStatement ps = conn.prepareStatement(query);
			// 쿼리는 여기서 실행되는 것이 아니고, execute 함수를 써야지만 실행됨
			//쿼리문이 이 때 실행되는 것은 아님, 미리 컴파일만 시켜놓는 것임
			System.out.println("3. PreparedStatement 생성");
			
			//4. 1)?에 값 바인딩 2) sqㅣ 실행.. 이때 디비에 데이터가 입력됨
			ps.setInt(1, 666); //(열, 값)
			ps.setString(2,"Martin");
			ps.setString(3,"Tokyo");
			
			int row=ps.executeUpdate(); //executeUpdate() : 수정된 열(int)이 반환됨 ~> 1아니면 0 출력
			System.out.println("4. "+row+" ROW Record 등록 성공");

		}catch(ClassNotFoundException e ){
			System.out.println("1. 드라이버 로딩 실패");
		}catch(SQLException e ){
			System.out.println("2. DB연결 실패");
		}
	}//JDBC4ProcessTest1 생성자
	
	public static void main(String[] args) {
		new JDBC4ProcessTest1();
	}//main

}//JDBC4ProcessTest1
