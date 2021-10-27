package com.heal.dashboard.service.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.heal.dashboard.service.entities.UserTimezonePojo;
import com.heal.dashboard.service.service.TimeZoneService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class UserTimeZoneController {

	@Autowired
	TimeZoneService timeZoneService;

	@ApiOperation(value = "ADD/Update user tagging Detail", response = String.class)
	 @ApiResponses(value = {@ApiResponse(code = 201, message = "Successfully created|Updated data"),
	            @ApiResponse(code = 500, message = "Internal Server Error"),
	            @ApiResponse(code = 400, message = "Invalid Request")})
	@PostMapping(value = "/users/{username}/timezones")
	public ResponseEntity<String> setUserPreferedZone(@PathVariable(name = "username") String userName,
			@RequestHeader(value = "Authorization") String header,
			@Valid @RequestBody UserTimezonePojo tzResponse) {			
			   String response = timeZoneService.setUserPreferredTimezone(userName, header, tzResponse);
				return new ResponseEntity<>(response, HttpStatus.CREATED);
				
	}

}

