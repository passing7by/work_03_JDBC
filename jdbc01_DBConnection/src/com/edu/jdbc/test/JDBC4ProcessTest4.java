package com.edu.jdbc.test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import com.mysql.cj.exceptions.RSAException;

import config.ServerInfo;
/*
 	서버주소
 	계정명
 	계정 비번
 	이런 값들은 프로그램 코드에 노출되면 안되는 값들임
 	프로그램에 이런 실제값이 들어가는 것 == 하드코딩이라고 함
 	
 	서버의 파편적인 정보를 외부에 모듈화 (메타데이터화) 시켜야 함
 	String(key) = String(value)///properties 파일 사용
 */
public class JDBC4ProcessTest4 {
	static String driver;
	static String url;
	static String user;
	static String pass;
	static String query;


	
	public JDBC4ProcessTest4() throws Exception{
		Class.forName(driver);
		System.out.println("1. 드라이버 로딩 성공");
		
		Connection conn = DriverManager.getConnection(url, user, pass);
		System.out.println("2. DB 연결 성공");

		PreparedStatement ps = conn.prepareStatement(query);
		
		ResultSet rs = ps.executeQuery();
		System.out.println("-------------------------------------");
		while(rs.next()) {
			System.out.println(rs.getInt("id")+"\t"
								+rs.getString("name")+"\t"
								+rs.getString("address"));
		}
		System.out.println("-------------------------------------");

	}
	
	public static void main(String[] args) throws Exception{
		new JDBC4ProcessTest4();
	}
	
	static {
		//1. Properties 파일을 로드함
		try {
			Properties p = new Properties();
			p.load(new FileInputStream("src/config/db.properties"));
			//src/config/db.properties -> FilePathSystem
			//config.ServerInfo -> ClassPathSystem
			
			driver = p.getProperty("jdbc.mqsql.driver");
			url = p.getProperty("jdbc.mqsql.url");
			user = p.getProperty("jdbc.mqsql.user");
			pass = p.getProperty("jdbc.mqsql.pass");
			query = p.getProperty("jdbc.sql.selectAll");


			Class.forName(ServerInfo.DRIVER_NAME);
			System.out.println("1. 드라이버 로딩 성공");
		}catch(Exception e){
			System.out.println("1. 드라이버 로딩 실패");
	
		}
	}
}
