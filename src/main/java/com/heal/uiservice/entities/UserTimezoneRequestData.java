package com.heal.uiservice.entities;

import lombok.Data;

@Data
public class UserTimezoneRequestData {
	    private UserTimezonePojo userTimezonePojo;
	    private String username;
	    private UserAttributeBeen userAttributeBeen;
	    private TimezoneDetail timezoneDetail;
	    private UserDetailsBean userDetailsBean;
}
