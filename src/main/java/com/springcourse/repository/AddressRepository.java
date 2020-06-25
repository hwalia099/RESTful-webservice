 package com.springcourse.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springcourse.entity.AddressEntity;
import com.springcourse.entity.UserEntity;
import com.springcourse.modelRequest.shared.dto.AddressDTO;
@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
	AddressEntity findByAddressId(String addressId);
}
