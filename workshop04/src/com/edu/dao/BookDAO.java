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
import com.edu.vo.Author;
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
			Class.forName(driver);
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
		Connection conn = DriverManager.getConnection(url, user, pass);
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
	
	//1. 책 추가
	public void insertBook(Book b) throws DMLException, DuplicateNumException {
		String query = "INSERT INTO book (isbn, title, authorno, publisher, price, description) VALUES (?, ?, ?, ?, ?, ?)";
		
		try(Connection conn = getConnection(); 
			PreparedStatement ps = conn.prepareStatement(query)) {
			
			ps.setString(1, b.getIsbn());
			ps.setString(2, b.getTitle());
			ps.setInt(3, b.getAuthorno());
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
	
	//2. 작가 추가
	public void insertAuthor(Author a) throws DMLException, DuplicateNumException {
		String query = "INSERT INTO author (authorno, name, phone) VALUES (?, ?, ?)";
		
		try(Connection conn = getConnection(); 
			PreparedStatement ps = conn.prepareStatement(query)) {
			
			ps.setInt(1, a.getAuthorno());
			ps.setString(2, a.getName());
			ps.setString(3, a.getPhone());


			System.out.println("[ Result OK Message ] => "+ps.executeUpdate() + "작가 등록 성공");
		
		}catch (SQLIntegrityConstraintViolationException e) { //중복오류
			throw new DuplicateNumException("[ Result Error Message ] => "+"이미 등록된 작가입니다. 다시 확인해주세요"); 
		
		}catch (SQLException e) { //문법오류
			throw new DMLException("[ Result Error Message ] => "+"작가 등록 중 문제가 발생해 등록이 이뤄지지 않았습니다."); 
		}
	}
	
	//3. 책 수정	
	public void updateBook(Book b) throws RecordNotFoundException, DMLException {
		String query = "UPDATE book SET title=?, authorno=?, publisher=?, price=?, description=? WHERE  isbn=?";
		try(Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query))  {
				
			ps.setString(1, b.getIsbn());
			ps.setString(2, b.getTitle());
			ps.setInt(3, b.getAuthorno());
			ps.setString(4, b.getPublisher());
			ps.setInt(5, b.getPrice());
			ps.setString(6, b.getDescription());
				
			if(ps.executeUpdate() ==0) {
				throw new RecordNotFoundException("[ Result Error Message ] => "+"정보를 수정할 책이 존재하지 않습니다.");
			}
			System.out.println("[ Result OK Message ] => "+"책 수정 성공");

				
		}catch (SQLException e) {
			throw new DMLException("[ Result Error Message ] => "+"책 정보 수정 중 문제가 발생해 수정이 이뤄지지 않았습니다.");
		}
	}	
	
	//4. 작가 수정
	public void updateAuthor(Author a) throws RecordNotFoundException, DMLException {	
		String query = "UPDATE author SET name=?, phone=? WHERE authorno=?";
		try(Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query))  {
				
			ps.setInt(1, a.getAuthorno());
			ps.setString(2, a.getName());
			ps.setString(3, a.getPhone());
				
			if(ps.executeUpdate() ==0) {
				throw new RecordNotFoundException("[ Result Error Message ] => "+"정보를 수정할 작가가 존재하지 않습니다.");
			}
			System.out.println("[ Result OK Message ] => "+"작가 수정 성공");

				
		}catch (SQLException e) {
			throw new DMLException("[ Result Error Message ] => "+"작가 정보 수정 중 문제가 발생해 수정이 이뤄지지 않았습니다.");
		}
	}	
	
	//5. 책 삭제
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
	
	//6. 작가 삭제
	public void deleteAuthor(int authorno) throws DMLException, RecordNotFoundException {
		String query = "delete from author where authorno =?";
		
		try(Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query))  {
			
			ps.setInt(1, authorno);
			
			if(ps.executeUpdate() ==0) {
				throw new RecordNotFoundException("[ Result Error Message ] => "+"삭제할 작가가 존재하지 않습니다.");
			}
			System.out.println("[ Result OK Message ] => "+"작가 삭제 성공");

				
		}catch (SQLException e) {
			throw new DMLException("[ Result Error Message ] => "+"작가 삭제 중 문제가 발생해 삭제가 이뤄지지 않았습니다.");
		}
	}
	
	////////////////////////////////////////////////// workshop 4 ////////////////////////////////////////////////////////////////////
	
	//7. 모든 책&작가 검색
	public List<String> listBooksAndAuthors() throws DMLException, RecordNotFoundException {
		List<String> list = new ArrayList<>();
				
		String query = "select concat(b.isbn, ' ', b.title, ' ', b.authorno, ' ', a.name, ' ', a.phone, ' ', b.publisher, ' ', b.price, ' ', b.description) from book b JOIN author a USING (authorno)";
		
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();){
			
			while(rs.next()) {
				list.add(rs.getString("concat(b.isbn, ' ', b.title, ' ', b.authorno, ' ', a.name, ' ', a.phone, ' ', b.publisher, ' ', b.price, ' ', b.description)"));
			}
			
			if(list.isEmpty()) {
				throw new RecordNotFoundException("[ Result Error Message ] => "+"등록된 책이나 작가가 존재하지 않습니다.");
			}
		}catch (SQLException e) {	
			throw new DMLException("[ Result Error Message ] => "+"책과 작가 검색 중 문제가 발생해 등록이 이뤄지지 않았습니다.");
		}
		return list;
	}
	//강사님 피드백 - EE급에서는, 정보를 객체 단위로 보내야 함 (받은 정보를 unpack해서 화면에 보여줘야 하기 때문)
	
	//8. 이름이 '?XX'인 저자의 도서명, isbn, 출판사 출력 기능
	public List<String> findBookAuthorOf(String name) throws DMLException, RecordNotFoundException {
		List<String> list = new ArrayList<>();
		
		String query = "select concat(a.name, ' ', b.title, ' ', b.isbn, ' ', b.publisher) from book b JOIN author a ON b.authorno=a.authorno where a.name like ?";
		
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query);){

			ps.setString(1, name+"%");
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				list.add(rs.getString("concat(a.name, ' ', b.title, ' ', b.isbn, ' ', b.publisher)"));
			}
			
			if(list.isEmpty()) {
				throw new RecordNotFoundException("[ Result Error Message ] => "+"등록된 책이나 작가가 존재하지 않습니다.");
			}
		}catch (SQLException e) {	
			throw new DMLException("[ Result Error Message ] => "+"책과 작가 검색 중 문제가 발생해 등록이 이뤄지지 않았습니다.");
		}
		return list;
	}
	
	//8-1. 이름이 '김XX'인 저자의 도서명, isbn, 출판사 출력 기능
	public List<String> findBookAuthorOfKim() throws DMLException, RecordNotFoundException {
		List<String> list = new ArrayList<>();
		
		String query = "select concat(a.name, ' ', b.title, ' ', b.isbn, ' ', b.publisher) from book b JOIN author a ON b.authorno=a.authorno where a.name like ?";
		
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query);){

			ps.setString(1, "김"+"%");
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				list.add(rs.getString("concat(a.name, ' ', b.title, ' ', b.isbn, ' ', b.publisher)"));
			}
			
			if(list.isEmpty()) {
				throw new RecordNotFoundException("[ Result Error Message ] => "+"등록된 책이나 작가가 존재하지 않습니다.");
			}
		}catch (SQLException e) {	
			throw new DMLException("[ Result Error Message ] => "+"책과 작가 검색 중 문제가 발생해 등록이 이뤄지지 않았습니다.");
		}
		return list;
	}
	
	
	//9. 모든 책&작가 검색 - 저자명별로 정보 출력
	public List<String> listBooksByAuthors() throws DMLException, RecordNotFoundException {
		List<String> list = new ArrayList<>();
				
		String query = "select concat(a.name, ' ', b.title, ' ', b.publisher, ' ', b.price) concat from book b JOIN author a USING (authorno) GROUP BY a.name, b.title, b.publisher, b.price ORDER BY a.name";
		
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();){
			
			while(rs.next()) {
				list.add(rs.getString("concat"));
			}
			
			if(list.isEmpty()) {
				throw new RecordNotFoundException("[ Result Error Message ] => "+"등록된 책이나 작가가 존재하지 않습니다.");
			}
		}catch (SQLException e) {	
			throw new DMLException("[ Result Error Message ] => "+"책과 작가 검색 중 문제가 발생해 등록이 이뤄지지 않았습니다.");
		}
		return list;
	}
	//count(a.name) 사용하지 않아도 결과 도출됨
	//concat() 뒤에 알리아스를 넣어주고, 그 알리아스를 rs.getString()에 넣어줘도 동작함
		
	//10. 책 제목 & 책 출판사 출력
	public List<String> listTitleAndPublisher() throws DMLException, RecordNotFoundException {
		List<String> list = new ArrayList<>();
				
		String query = "select concat(title, '은 ', publisher, '에서 출판했다') from book";
		
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();){
			
			while(rs.next()) {
				list.add(rs.getString("concat(title, '은 ', publisher, '에서 출판했다')"));
			}
			
			if(list.isEmpty()) {
				throw new RecordNotFoundException("[ Result Error Message ] => "+"등록된 책이 존재하지 않습니다.");
			}
		}catch (SQLException e) {	
			throw new DMLException("[ Result Error Message ] => "+"책 검색 중 문제가 발생해 등록이 이뤄지지 않았습니다.");
		}
		return list;
	}

}
