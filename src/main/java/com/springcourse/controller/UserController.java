package com.springcourse.controller;

import java.util.ArrayList;
import java.util.List;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springcourse.exceptions.UserServiceException;
import com.springcourse.modelRequest.PasswordResetModel;
import com.springcourse.modelRequest.PasswordResetRequestModel;
import com.springcourse.modelRequest.UserDetailsRequestModel;
import com.springcourse.modelRequest.shared.AmazonSES;
import com.springcourse.modelRequest.shared.dto.AddressDTO;
import com.springcourse.modelRequest.shared.dto.UserDto;
import com.springcourse.modelResponse.AddressesRest;
import com.springcourse.modelResponse.OperationStatusModel;
import com.springcourse.modelResponse.RequestOperationStatus;
import com.springcourse.modelResponse.UserRest;
import com.springcourse.service.AddressService;
import com.springcourse.service.UserService;
import com.springcourse.modelResponse.AddressesRest;
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	AddressService addressesService;

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }) // id
																													// ki
																													// value
																													// pass
																													// hogi
																													// userID
																													// ki
																													// localhost/user/userID
																													// alphanumeric
																													// value
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserByUserId(id);
		// UserDto userDto = userRepository.getUserByEmail(id);
		BeanUtils.copyProperties(userDto, returnValue);

		return returnValue;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValue = new UserRest();
		 System.out.println(userDetails.getFristName()+ "firstname");
		// UserDto userDto = new UserDto();
		// BeanUtils.copyProperties(userDetails, userDto);
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);
		returnValue = modelMapper.map(createdUser, UserRest.class);
         
		return returnValue;
	}

	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })

	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails)
			throws Exception {
		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();
		if (userDetails.getFristName().isEmpty())
			throw new UserServiceException("name param esmpty");
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto updateUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updateUser, returnValue);
		return returnValue;

	}

	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteUser(id);

		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}

	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "2") int limit) {
		List<UserRest> returnValue = new ArrayList<>();

		List<UserDto> users = userService.getUsers(page, limit);

		// returnValue = new ModelMapper().map(users, listType);

		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);

		}
		return returnValue;
	}

	@GetMapping(path = "/{id}/addresses", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE , "application/hal+json"})
	public Resources<AddressesRest> getUserAddresses(@PathVariable String id) {
		List<AddressesRest> returnValue = new ArrayList<>();

		List<AddressDTO> addressesDTO = addressesService.getAddresses(id);

		if (addressesDTO != null && !addressesDTO.isEmpty()) {
			java.lang.reflect.Type listType = new TypeToken<List<AddressesRest>>() {
			}.getType();
			returnValue = new ModelMapper().map(addressesDTO, listType);
		}
		for(AddressesRest as: returnValue)
		{
			Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, as.getAddressId()))
					.withSelfRel();

			Link userLink = linkTo(UserController.class).slash(id).withRel("user");
			as.add(addressLink);
		as.add(userLink);
		}
		return new Resources<>(returnValue);

	}

	@GetMapping(path = "/{userId}/addresses/{addressId}",  produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }) 
		public Resource<AddressesRest> getUserAddress( @PathVariable String userId,@PathVariable String addressId) {
           //AddressesRest rv=null;
			AddressDTO addressesDto = addressesService.getAddress(addressId);

			ModelMapper modelMapper = new ModelMapper();
			//Link addressLink = linkTo(UserController.class).slash(userId).withSelfRel();
			//Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
			//Link addressesLink = linkTo(UserController.class).slash(userId).slash("addresses").withRel("addresses");
			Link addressLink = linkTo(methodOn((UserController.class)).getUserAddress(userId, addressId)).withSelfRel();
			Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
			Link addressesLink = linkTo(methodOn((UserController.class)).getUserAddresses(userId)).withRel("addresses");
			AddressesRest addressesRestModel = modelMapper.map(addressesDto, AddressesRest.class);
         //Mthod on is a alternatve and better aprroch to slash one
			addressesRestModel.add(addressLink);
			addressesRestModel.add(userLink);
			addressesRestModel.add(addressesLink);
 //here links comes as  _link in postman with just one field herf 
			//while same linktomehtod will be like links(no underscre) and with sevreal other fields like type ,medis,title etc
			//coz it returns collection of addresses 
			return new Resource <>(addressesRestModel);

	}	
	 /*
     * http://localhost:8080/mobile-app-ws/users/email-verification?token=sdfsdf
     * */
    @GetMapping(path = "/email-verification", produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE })
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
        System.out.println(token);
        boolean isVerified = userService.verifyEmailToken(token);
        
        if(isVerified)
        {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;
    }
    @PostMapping(path = "/password-reset-request", 
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {
    	OperationStatusModel returnValue = new OperationStatusModel();
 
        boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());
        
        returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
 
        if(operationResult)
        {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }
    @PostMapping(path = "/password-reset",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
    	OperationStatusModel returnValue = new OperationStatusModel();
 
        boolean operationResult = userService.resetPassword(
                passwordResetModel.getToken(),
                passwordResetModel.getPassword());
        
        returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
 
        if(operationResult)
        {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }


}
