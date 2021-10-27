package com.heal.dashboard.service.entities;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountServiceKey {
	  	private int accountId;
	    private int serviceId;
}
