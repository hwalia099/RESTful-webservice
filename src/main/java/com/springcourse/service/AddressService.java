package com.springcourse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springcourse.modelRequest.shared.dto.AddressDTO;

public interface AddressService {
	//List<AddressDTO> getAddresses(String userId);
    AddressDTO getAddress(String addressId);
//	List<AddressDTO> getAddresses(String id);
	List<AddressDTO> getAddresses(String id);
	//AddressDTO getAddress(String addressId);
}

