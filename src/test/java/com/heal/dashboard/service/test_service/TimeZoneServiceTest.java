package com.heal.dashboard.service.test_service;

import com.heal.dashboard.service.businesslogic.timezone.UserTimezoneBL;
import com.heal.dashboard.service.exception.ClientException;
import com.heal.dashboard.service.exception.CustomExceptionHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import com.heal.dashboard.service.entities.UserAttributeBeen;
import com.heal.dashboard.service.entities.UserTimezonePojo;
import com.heal.dashboard.service.entities.UserTimezoneRequestData;
import com.heal.dashboard.service.entities.UtilityBean;
import com.heal.dashboard.service.service.TimeZoneService;

@RunWith(SpringRunner.class)
public class TimeZoneServiceTest {

	@InjectMocks
	TimeZoneService timeZoneService;

	@Mock
    UserTimezoneBL setUserTimeZoneBL;

	private UserTimezoneRequestData userTimezoneRequestData;

	@Before
	public void setup() {
		// setting up mock data for client validation response
		UserTimezonePojo tzResponse = new UserTimezonePojo();
		tzResponse.setIsNotificationsTimezoneMychoice(1);
		tzResponse.setIsTimezoneMychoice(1);
		tzResponse.setTimezoneId(0);
		userTimezoneRequestData = new UserTimezoneRequestData();
		userTimezoneRequestData.setUserTimezonePojo(tzResponse);
		userTimezoneRequestData.setUsername("appsoneadmin");

		// setting up mock data for serverValidation response
		UserAttributeBeen userAttributeBeen = new UserAttributeBeen();
		userAttributeBeen.setId(1);
		userAttributeBeen.setIsTimezoneMychoice(1);
		userAttributeBeen.setIsNotificationsTimezoneMychoice(0);
		userAttributeBeen.setIsTimezoneMychoice(0);
		userAttributeBeen.setStatus(1);
		userTimezoneRequestData.setUserAttributeBeen(userAttributeBeen);

	}

	@Test
	public void setUserPreferredTimezone() throws Exception {
		Mockito.when(setUserTimeZoneBL.clientValidation(Mockito.any()))
				.thenReturn(UtilityBean.<UserTimezoneRequestData>builder()
						.authToken("7640123a-fbde-4fe5-9812-581cd1e3a9c1").pojoObject(userTimezoneRequestData).build());
		Mockito.when(setUserTimeZoneBL.serverValidation(Mockito.any())).thenReturn(userTimezoneRequestData);
		Mockito.when(setUserTimeZoneBL.process(Mockito.any())).thenReturn("7640123a-fbde-4fe5-9812-581cd1e3a9c1");
		Assert.assertEquals("7640123a-fbde-4fe5-9812-581cd1e3a9c1", timeZoneService
				.setUserPreferredTimezone("testing", "testing", userTimezoneRequestData.getUserTimezonePojo()));
	}

	@Test(expected = CustomExceptionHandler.class)
	public void getAccountList_BadRequestException() throws ClientException {
		Mockito.when(setUserTimeZoneBL.clientValidation(Mockito.any()))
				.thenThrow(new ClientException("authentication failed"));
		timeZoneService.setUserPreferredTimezone(null, null, null);
	}

	@Test(expected = RuntimeException.class)
	public void getAccountList_InternalServerError() throws Exception {
		Mockito.when(setUserTimeZoneBL.clientValidation(Mockito.any()))
				.thenReturn(UtilityBean.<UserTimezoneRequestData>builder()
						.authToken("7640123a-fbde-4fe5-9812-581cd1e3a9c1").pojoObject(userTimezoneRequestData).build());
		Mockito.when(setUserTimeZoneBL.serverValidation(Mockito.any())).thenReturn(userTimezoneRequestData);
		Mockito.when(setUserTimeZoneBL.process(Mockito.any())).thenThrow(new RuntimeException());
		timeZoneService.setUserPreferredTimezone(null, null, null);
	}
}
