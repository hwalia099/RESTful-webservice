package com.springcourse.modelRequest;

import java.util.List;

public class UserDetailsRequestModel {
	private String email;
	private String fristName;
	private String lastName;
	private String password;
	private List<AddressRequestModel> addresses;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFristName() {
		return fristName;
	} 
	public List<AddressRequestModel> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<AddressRequestModel> addresses) {
		this.addresses = addresses;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
