package com.springcourse.repository;

import org.springframework.data.repository.CrudRepository;

import com.springcourse.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long>{   
	//   PasswordResetTokenEntity is the datatype of object this ll return


	PasswordResetTokenEntity findByToken(String token);

}
