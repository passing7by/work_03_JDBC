package com.edu.test;

import java.sql.SQLException;

import com.edu.dao.impl.EmployeeDAOImpl;
import com.edu.exception.DuplicateNumException;
import com.edu.vo.Employee;

public class EmployeeDAOTest {


	public static void main(String[] args){
		
		EmployeeDAOImpl dao = EmployeeDAOImpl.getInstance();
		try {
			dao.insertEmployee(new Employee(111, "JAMES", 1500.0, "NY"));
			
		}catch (DuplicateNumException e) {
			System.out.println(e.getMessage());
			
		}catch (SQLException e) {
			System.out.println("쿼리문 실행도중 알 수 없는 문제가 발생했습니다.");
		}
		
//		dao.insertEmployee(new Employee(111, "JAMES", 1500.0, "NY"));
//		dao.insertEmployee(new Employee(222, "Tomas", 800.0, "Brandon"));
//		dao.insertEmployee(new Employee(333, "PAPAPA", 2500.0, "PARIS"));
//		dao.insertEmployee(new Employee(444, "KIM", 1900.0, "KOREA"));
		
//		System.out.println(dao.selectEmployee(111));
//		System.out.println("==========================================");
//		dao.selectEmployee().forEach(System.out::println);
//		
//		System.out.println("\n==========================================\n");
//		
//		dao.updateEmployee(new Employee(111, "JAMES", 1200.0, "NY"));
//		
//		System.out.println(dao.selectEmployee(111));
//		System.out.println("==========================================");
//		dao.selectEmployee().forEach(System.out::println);
//		
//		System.out.println("\n==========================================\n");
//		
//		dao.removeEmployee(222);
//		dao.selectEmployee().forEach(System.out::println);
		
	}

}
