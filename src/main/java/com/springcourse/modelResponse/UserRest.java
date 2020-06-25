package com.springcourse.modelResponse;

import java.util.List;

public class UserRest {
	private String email;
	private String fristName;
	private String lastName;
	private String userId;
	private List<AddressesRest> addresses;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFristName() {
		return fristName;
	}
	public void setFristName(String fristName) {
		this.fristName = fristName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<AddressesRest> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<AddressesRest> addresses) {
		this.addresses = addresses;
	}
	
}
