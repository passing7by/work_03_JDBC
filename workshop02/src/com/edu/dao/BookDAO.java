package com.edu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import com.edu.exception.DMLException;
import com.edu.exception.DuplicateNumException;
import com.edu.exception.RecordNotFoundException;
import com.edu.vo.Book;

import config.ServerInfo;

public class BookDAO {
	
	static String driver = ServerInfo.DRIVER_NAME;
	static String url =ServerInfo.URL;
	static String user = ServerInfo.USER;
	static String pass = ServerInfo.PASSWORD;
	
	//싱글톤
	private static BookDAO dao = new BookDAO();
	private BookDAO() {
		try {
			Class.forName(ServerInfo.DRIVER_NAME);
			System.out.println("1. 드라이버 로딩 성공");
			
		} catch (ClassNotFoundException e) {
			System.out.println("1. 드라이버 로딩 성공");
		}
	}
	public static BookDAO getInstance() {
		return dao;
	}
	
	//////////////////////////////// 공통 로직(고정적인 부분) //////////////////////////////////
	public Connection getConnection() throws SQLException{
		Connection conn = DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASSWORD);
		System.out.println("2. DB 연결 성공");
		return conn;
	}
	
	public void close(Connection conn) throws SQLException {
		if( conn != null) conn.close();
	}
	
	public void close(PreparedStatement ps) throws SQLException {
		if( ps != null) ps.close();
	}
	
	public void close(ResultSet rs) throws SQLException {
		if( rs != null) rs.close();
	}
	
	////////////////////////////////비즈니스 로직(가변적인 부분) //////////////////////////////////
		
	public void insertBook(Book b) throws DMLException, DuplicateNumException {
		String query = "INSERT INTO book (isbn, title, author, publisher, price, description) VALUES(?, ?, ?, ?, ?, ?)";
		
		try(Connection conn = getConnection(); 
			PreparedStatement ps = conn.prepareStatement(query)) {
			
			ps.setString(1, b.getIsbn());
			ps.setString(2, b.getTitle());
			ps.setString(3, b.getAuthor());
			ps.setString(4, b.getPublisher());
			ps.setInt(5, b.getPrice());
			ps.setString(6, b.getDescription());
		
			System.out.println("[ Result OK Message ] => "+ps.executeUpdate() + " 책 등록 성공");
		
		}catch (SQLIntegrityConstraintViolationException e) { //중복오류
			throw new DuplicateNumException("[ Result Error Message ] => "+"이미 등록된 책입니다. 다시 확인해주세요"); 
		
		}catch (SQLException e) { //문법오류
			throw new DMLException("[ Result Error Message ] => "+"책 등록 중 문제가 발생해 등록이 이뤄지지 않았습니다."); 
		}
	}
	
	public void updateBook(Book b) throws RecordNotFoundException, DMLException {
		
		String query = "UPDATE book SET title=?, author=?, publisher=?, price=?, description=? WHERE isbn=?";
		try(Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query))  {
				
			ps.setString(1, b.getTitle());
			ps.setString(2, b.getAuthor());
			ps.setString(3, b.getPublisher());
			ps.setInt(4, b.getPrice());
			ps.setString(5, b.getDescription());
			ps.setString(6, b.getIsbn());
				
			if(ps.executeUpdate() ==0) {
				throw new RecordNotFoundException("[ Result Error Message ] => "+"정보를 수정할 책이 존재하지 않습니다.");
			}
			System.out.println("[ Result OK Message ] => "+" 책 수정 성공");

				
		}catch (SQLException e) {
			throw new DMLException("[ Result Error Message ] => "+"책 정보 수정 중 문제가 발생해 수정이 이뤄지지 않았습니다.");
		}
	
	}
	
	public void deleteBook(String isbn) throws DMLException, RecordNotFoundException {
		
		String query = "delete from book where isbn =?";
		
		try(Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query))  {
			
			ps.setString(1, isbn);
			
			if(ps.executeUpdate() ==0) {
				throw new RecordNotFoundException("[ Result Error Message ] => "+"삭제할 책이 존재하지 않습니다.");
			}
			System.out.println("[ Result OK Message ] => "+" 책 삭제 성공");

				
		}catch (SQLException e) {
			throw new DMLException("[ Result Error Message ] => "+"책 삭제 중 문제가 발생해 삭제가 이뤄지지 않았습니다.");
		}
	
	}
	
	
	public Book findBook(String isbn) throws DMLException, RecordNotFoundException {
		Book b = null;
		
		String query = "select isbn, title, author, publisher, price, description from book where isbn = trim(?)";
		
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query);){
			
			ps.setString(1, isbn);
			
			ResultSet rs = ps.executeQuery(); //counnection은 무조건 닫아야함, ResultSet은 안 닫아도 상관X
			
			if(rs.next()) { //찾고자 하는 책이 있다면
				b = new Book(isbn,
							 rs.getString("title"),
							 rs.getString("author"),
							 rs.getString("publisher"),
							 rs.getInt("price"),
							 rs.getString("description"));
			}else { //찾고자 하는 책이 없다면
				throw new RecordNotFoundException("[ Result Error Message ] => "+"검색할 책이 존재하지 않습니다.");
			}
		}catch (SQLException e) {
			throw new DMLException("[ Result Error Message ] => "+"책 검색 중 문제가 발생해 검색이 이뤄지지 않았습니다.");
		}
		
		return b;
	}
	
	public List<Book> listBooks() throws DMLException, RecordNotFoundException {
		List<Book> list = new ArrayList<>();
				
		String query = "select isbn, title, author, publisher, price, description from book";
		
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();){
			
			while(rs.next()) {
				list.add( new Book(rs.getString("isbn"),
									 rs.getString("title"),
									 rs.getString("author"),
									 rs.getString("publisher"),
									 rs.getInt("price"),
									 rs.getString("description")));
			}
			
			if(list.isEmpty()) {
				throw new RecordNotFoundException("[ Result Error Message ] => "+"등록된 책이 존재하지 않습니다.");
			}
		}catch (SQLException e) {	
			throw new DMLException("[ Result Error Message ] => "+"책 검색 중 문제가 발생해 등록이 이뤄지지 않았습니다.");
		}
		return list;
	}
	
	
	//총 도서갯수를 구하는 비지니스 로직...count() 행의 수를 리턴하는 그룹함수 --> while ++ X
	public int count() throws DMLException{
		String query = "select count(isbn) count from book"; //알리아스 명으로는 그룹함수 명을 사용하는 것이 좋음
		
		int count = 0;
		
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			){
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) count = rs.getInt("count"); //알리아스 안하고 '1'이라고 작성해도 됨
			
		}catch (SQLException e) {	
			throw new DMLException("[ Result Error Message ] => "+"책 검색 중 문제가 발생해 등록이 이뤄지지 않았습니다.");
		}
		return count;
	}
}
