package com.heal.dashboard.service.entities;


import lombok.Data;

@Data
public class ViewApplicationServiceMappingBean {
	  	private int applicationId;
	    private String applicationName;
	    private String applicationIdentifier;
	    private int serviceId;
	    private String serviceName;
	    private String serviceIdentifier;
	    private int accountId;
	
}