package com.edu.exception;

public class RecordNotFoundException extends Exception{
	
	public RecordNotFoundException( String msg) {
		super(msg);
	}
	
	public RecordNotFoundException() {
		this("존재하는 사람이 없습니다.");
	}
	

}
