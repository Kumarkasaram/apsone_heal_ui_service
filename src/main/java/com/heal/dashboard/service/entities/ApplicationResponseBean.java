package com.heal.dashboard.service.entities;



import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicationResponseBean {
	private int accountId;
	private List<Controller> controllerBeanList;
	List<TxnAndGroupBean> appTxnDetails ;
	private UserAccessDetails userAccessDetails;
	
}
