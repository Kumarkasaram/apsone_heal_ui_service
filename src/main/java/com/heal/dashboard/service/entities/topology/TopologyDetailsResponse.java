package com.heal.dashboard.service.entities.topology;



import lombok.Data;

@Data
public class TopologyDetailsResponse {
	 	String responseStatus;
	    String responseMessage;
	    TopologyDetails data;
}
