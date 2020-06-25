package com.springcourse.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 

import com.springcourse.entity.UserEntity;
@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
     UserEntity findByEmail(String email);
     UserEntity findByUserId(String userId);
     UserEntity findUserByEmailVerificationToken(String token);
}
