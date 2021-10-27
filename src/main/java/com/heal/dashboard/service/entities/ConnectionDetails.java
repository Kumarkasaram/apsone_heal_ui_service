package com.heal.dashboard.service.entities;



import lombok.Data;

@Data
public class ConnectionDetails {
		private int id;
	    private int sourceId;
	    private String sourceRefObject;
	    private int destinationId;
	    private String destinationRefObject;
	    private int accountId;
	    private String userDetailsId;
	
}

