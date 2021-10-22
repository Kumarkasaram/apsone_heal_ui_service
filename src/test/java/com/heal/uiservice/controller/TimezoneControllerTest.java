package com.heal.uiservice.controller;



import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.heal.uiservice.controller.UserTimeZoneController;
import com.heal.uiservice.entities.UserTimezonePojo;
import com.heal.uiservice.pojo.ResponseBean;
import com.heal.uiservice.service.TimeZoneService;

@RunWith(SpringRunner.class)
public class TimezoneControllerTest {

	 @InjectMocks
	    UserTimeZoneController timezoneController;
	    @Mock
	    TimeZoneService timeZoneService;
	   
	    
	   

	    @Before
	    public void setup() {
//	      setting up mock data in  accountBean
	  
	    }
	
	    @Test
		public void setUserPreferredTimezone_OK() throws Exception {
					Mockito.when(timeZoneService.setUserPreferredTimezone(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn("7640123a-fbde-4fe5-9812-581cd1e3a9c1");
			Assert.assertEquals(HttpStatus.CREATED, timezoneController.setUserPreferedZone("appsone", "header", new UserTimezonePojo()).getStatusCode());
		}
}
