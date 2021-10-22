package com.heal.uiservice.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heal.uiservice.businesslogic.UserTimezoneBL;
import com.heal.uiservice.entities.UserTimezonePojo;
import com.heal.uiservice.entities.UserTimezoneRequestData;
import com.heal.uiservice.entities.UtilityBean;
import com.heal.uiservice.exception.ClientException;
import com.heal.uiservice.exception.DataProcessingException;
import com.heal.uiservice.exception.CustomExceptionHandler;
import com.heal.uiservice.exception.ServerException;
import com.heal.uiservice.pojo.RequestObject;

@Service
public class TimeZoneService {

	@Autowired
	UserTimezoneBL userTimezoneBL;

	public String setUserPreferredTimezone(String userName, String requestHeader, UserTimezonePojo tzResponse) {
		String userIdentifier = null;
		try {
			RequestObject<UserTimezoneRequestData> requestObject = new RequestObject<UserTimezoneRequestData>();
			UserTimezoneRequestData userTimezoneRequestData = new UserTimezoneRequestData();
			userTimezoneRequestData.setUserTimezonePojo(tzResponse);
			userTimezoneRequestData.setUsername(userName);
			requestObject.addHeaders("Authorization", requestHeader);
			requestObject.setBody(userTimezoneRequestData);
			
			UtilityBean<UserTimezoneRequestData> utilityBean = userTimezoneBL.clientValidation(requestObject);
			userTimezoneRequestData = userTimezoneBL.serverValidation(utilityBean);
			userIdentifier = userTimezoneBL.process(userTimezoneRequestData);
			
		} catch (ServerException | DataProcessingException | ClientException  e) {
			throw new CustomExceptionHandler(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return userIdentifier;
	}

}
