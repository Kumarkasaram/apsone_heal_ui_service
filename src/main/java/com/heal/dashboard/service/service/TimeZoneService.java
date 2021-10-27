package com.heal.dashboard.service.service;



import com.heal.dashboard.service.businesslogic.timezone.UserTimezoneBL;
import com.heal.dashboard.service.entities.UserTimezonePojo;
import com.heal.dashboard.service.exception.ClientException;
import com.heal.dashboard.service.exception.CustomExceptionHandler;
import com.heal.dashboard.service.exception.DataProcessingException;
import com.heal.dashboard.service.exception.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heal.dashboard.service.entities.UserTimezoneRequestData;
import com.heal.dashboard.service.entities.UtilityBean;
import com.heal.dashboard.service.pojo.RequestObject;

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
			
		} catch (ServerException | DataProcessingException | ClientException e) {
			throw new CustomExceptionHandler(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return userIdentifier;
	}

}
