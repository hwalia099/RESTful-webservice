package com.springcourse.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.springcourse.entity.UserEntity;
import com.springcourse.modelRequest.shared.Utils;
import com.springcourse.modelRequest.shared.dto.AddressDTO;
import com.springcourse.modelRequest.shared.dto.UserDto;
import com.springcourse.repository.UserRepository;

class UserServiceImplTest {
	@InjectMocks
	UserServiceImpl userService;
	@Mock
	UserRepository userRepository;
	@Mock
	Utils utils;
	// @bean is used for autowiring
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	String encryptedPassword = "sasadasad1";
	String userId = "sadq2das";
	UserEntity userEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFristName("himanshu");
		userEntity.setLastName("Kaolov");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationToken("7htnfhr758");
	}

	@Test
	final void testGetUser() {
		// fail("Not yet implemented");
		UserEntity userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFristName("himanshu");
		userEntity.setLastName("Kaolov");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		UserDto userDto = userService.getUser("jaihind");
		assertNotNull(userDto);
		assertEquals("himanshu", userDto.getFristName());
	}

	@Test
	final void testGetUser_UsernameNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		assertThrows(UsernameNotFoundException.class, () -> {
			userService.getUser("jaihind");
		});

	}

	@Test
	final void testCreateUser() {

		when(userRepository.findByEmail(anyString())).thenReturn(null);

		when(utils.generateAddressId(anyInt())).thenReturn("asdsadasd");
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		
		AddressDTO addresses=new AddressDTO();
		addresses.setType("shipping");
		 
		List<AddressDTO > l=new ArrayList<>();
		l.add(addresses);
		
		UserDto d= new UserDto();
		d.setAddresses(l);
		
		UserDto dt=userService.createUser(d);
         assertNotNull(dt);
         assertEquals(userEntity.getFristName(),d.getFristName());
	}

}