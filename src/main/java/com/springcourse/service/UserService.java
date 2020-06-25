package com.springcourse.service;

  
import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.springcourse.modelRequest.shared.dto.UserDto;
//to implement it another packge service.impl
public interface UserService extends UserDetailsService { 
	UserDto createUser(UserDto user);
     UserDto getUser(String email);
	UserDto getUserByUserId(String userId);
	UserDto updateUser(String id, UserDto userDto);
	void deleteUser(String id);
	//List<UserDto> getUsers(int page,int limit);
	List<UserDto> getUsers(int page, int limit);
	boolean verifyEmailToken(String token);
	
	boolean requestPasswordReset(String email);

	boolean resetPassword(String token, String password);
}
