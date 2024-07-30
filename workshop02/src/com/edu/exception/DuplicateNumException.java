package com.edu.exception;

public class DuplicateNumException extends Exception{
	
	public DuplicateNumException( String msg) {
		super(msg);
	}
	
	public DuplicateNumException() {
		this("Dupulicate Number 이미 존재하는 사람입니다.");
	}
	

}
