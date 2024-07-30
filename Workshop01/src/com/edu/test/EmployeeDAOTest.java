package com.edu.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.edu.dao.impl.EmployeeDAOImpl;
import com.edu.vo.Employee;

import config.ServerInfo;

public class EmployeeDAOTest {

	public static void main(String[] args) {
		EmployeeDAOImpl service = EmployeeDAOImpl.getInstance();
		
		try{
			System.out.println("============= insertEmployee() ============="); 
//			service.insertEmployee(new Employee(1, "Jenny", 1200.0, "Seoul"));
//			service.insertEmployee(new Employee(2, "Jonh", 1000.0, "NY"));
//			service.insertEmployee(new Employee(3, "Jinam", 500.0, "Daegu"));
			
			System.out.println("============= removeEmployee() ============="); 
			service.removeEmployee(1);
			
			System.out.println("============= updateEmployee() ============="); 
			service.updateEmployee(new Employee(2, "Jenny", 1500.0, "Seoul"));
			
			System.out.println("============= selectEmployee() ============="); 
			ArrayList<Employee> list1 = service.selectEmployee();
			System.out.println(list1);
			
			System.out.println("============= selectEmployee() ============="); 
			ArrayList<Employee> list2 = service.selectEmployee(3);
			System.out.println(list2);
			
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * static { try { Class.forName(ServerInfo.DRIVER_NAME);
	 * System.out.println("1. 드라이버 로딩 성공"); }catch(ClassNotFoundException e){
	 * System.out.println("1. 드라이버 로딩 실패"); } }
	 */

}
