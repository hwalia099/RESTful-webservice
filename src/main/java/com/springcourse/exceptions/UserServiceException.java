package com.springcourse.exceptions;


public class UserServiceException extends RuntimeException{
 
	private static final long serialVersionUID = 969485660789690916L;

	public UserServiceException(String message)
	{
		super(message);
	}
}

