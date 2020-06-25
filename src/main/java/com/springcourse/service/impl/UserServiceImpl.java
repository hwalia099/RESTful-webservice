package com.springcourse.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.PageRequest; 
import org.springframework.data.domain.Pageable; 

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springcourse.entity.PasswordResetTokenEntity;
import com.springcourse.entity.UserEntity;
import com.springcourse.exceptions.UserServiceException;
import com.springcourse.modelRequest.shared.dto.AddressDTO;
import com.springcourse.modelRequest.shared.dto.UserDto;
import com.springcourse.modelResponse.ErrorMessages;
import com.springcourse.repository.PasswordResetTokenRepository;
import com.springcourse.repository.UserRepository;

import com.springcourse.service.UserService;
import com.springcourse.modelRequest.shared.AmazonSES;
import com.springcourse.modelRequest.shared.Utils;
//import org.springframework.security.core.userdetails.User;
//import antlr.Utils;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    Utils utils;
    // @bean is used for autowiring
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
	@Autowired
	UserRepository userRepository;
	@Override
	public UserDto createUser(UserDto user) {

		if (userRepository.findByEmail(user.getEmail()) != null)
			throw new UserServiceException("Record already exists");

		for(int i=0;i<user.getAddresses().size();i++)
		{
			AddressDTO address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(30));
			user.getAddresses().set(i, address);
		}
		  
		//BeanUtils.copyProperties(user, userEntity);
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);

		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
        userEntity.setEmailVerificationStatus(false);
		UserEntity storedUserDetails = userRepository.save(userEntity);
 
		//BeanUtils.copyProperties(storedUserDetails, returnValue);
		UserDto returnValue  = modelMapper.map(storedUserDetails, UserDto.class);
		
        // Send an email message to user to verify their email address
		//amazonSES.verifyEmail(returnValue);
		 new AmazonSES().verifyEmail(returnValue); 
		return returnValue;
	}

	@Override
	public UserDto getUser(String email)
	{
		UserEntity userEntity=userRepository.findByEmail(email);
		if(userEntity==null) throw new UsernameNotFoundException(email);
		UserDto rv=new UserDto();
		BeanUtils.copyProperties(userEntity,rv);
		return rv;
		
	}
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UserEntity userEntity=userRepository.findByEmail(email);
		if(userEntity==null) throw new UsernameNotFoundException(email);

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), 
				 new ArrayList<>());

	//	return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),new ArrayList<>());
			//token will be included as header in http request anbd resposne to authorize the reques	
	}  
		@Override
		public UserDto getUserByUserId(String userId) {
			UserDto returnValue = new UserDto();
			UserEntity userEntity = userRepository.findByUserId(userId);

			if (userEntity == null)
				throw new UsernameNotFoundException("User with ID: " + userId + " not found");

			BeanUtils.copyProperties(userEntity, returnValue);

			return returnValue;
		}
		@Override
		public UserDto updateUser(String userId, UserDto userDto) {
			UserDto returnValue = new UserDto();
			UserEntity userEntity = userRepository.findByUserId(userId);
			if (userEntity == null)
				throw new UsernameNotFoundException("User with ID: " + userId + " not found");
			userEntity.setFristName(userDto.getFristName());
			userEntity.setLastName(userDto.getLastName());
			userEntity=userRepository.save(userEntity);
			BeanUtils.copyProperties(userEntity, returnValue);
			return returnValue;
		}
		
			@Override 
			 	public void deleteUser(String userId) { 
					UserEntity userEntity = userRepository.findByUserId(userId); 
			 
			 
			 		if (userEntity == null) 
						throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()); 
			 
			 
			 		userRepository.delete(userEntity); 
			 
			 
				}
			@Override
			public List<UserDto> getUsers(int page, int limit) {

				// TODO Auto-generated method stub 
				List<UserDto> returnValue = new ArrayList<>();
				if(page>0) page=page-1;

				Pageable pageableRequest = PageRequest.of(page, limit);
				
				Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
				List<UserEntity> users = usersPage.getContent();
				
		        for (UserEntity userEntity : users) {
		            UserDto userDto = new UserDto();
		            BeanUtils.copyProperties(userEntity, userDto);
		            returnValue.add(userDto);
		        }
				
				return returnValue;
			}

			@Override
			public boolean verifyEmailToken(String token) {
			    boolean returnValue = false;

		        // Find user by token
		        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

		        if (userEntity != null) {
		            boolean hastokenExpired = Utils.hasTokenExpired(token);
		            if (!hastokenExpired) {
		                userEntity.setEmailVerificationToken(null);
		                userEntity.setEmailVerificationStatus(Boolean.TRUE);
		                userRepository.save(userEntity);
		                returnValue = true;
		            }
		        }
		        return returnValue;
			}

			@Override
			public boolean requestPasswordReset(String email) {
				
		        boolean returnValue = false;
		        
		        UserEntity userEntity = userRepository.findByEmail(email);

		        if (userEntity == null) {
		            return returnValue;
		        }
		        
		        String token = new Utils().generatePasswordResetToken(userEntity.getUserId());
		        
		        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
		        passwordResetTokenEntity.setToken(token);
		        passwordResetTokenEntity.setUserDetails(userEntity);
		        passwordResetTokenRepository.save(passwordResetTokenEntity);
		        
		        returnValue = new AmazonSES().sendPasswordResetRequest(
		                userEntity.getFristName(), 
		                userEntity.getEmail(),
		                token);
		        
				return returnValue;
			}
			@Override
			public boolean resetPassword(String token, String password) {
		        boolean returnValue = false;
		        
		        if( Utils.hasTokenExpired(token) )
		        {
		            return returnValue;
		        }
		 
		        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

		        if (passwordResetTokenEntity == null) {
		            return returnValue;
		        }

		        // Prepare new password
		        String encodedPassword = bCryptPasswordEncoder.encode(password);
		        
		        // Update User password in database
		        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
		        userEntity.setEncryptedPassword(encodedPassword);
		        userEntity.setPassword(password);
		      //  userEntity.setLastName(password);
		        UserEntity savedUserEntity = userRepository.save(userEntity);
		 
		        // Verify if password was saved successfully
		        if (savedUserEntity != null && savedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
		            returnValue = true;
		        }
		   
		        // Remove Password Reset token from database
		        passwordResetTokenRepository.delete(passwordResetTokenEntity);
		        
		        return returnValue;
			}
}
	

	


