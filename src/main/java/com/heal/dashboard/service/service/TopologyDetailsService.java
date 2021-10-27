package com.heal.dashboard.service.service;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heal.dashboard.service.entities.topology.TopologyDetails;
import com.heal.dashboard.service.entities.topology.TopologyDetailsResponse;
import com.heal.dashboard.service.entities.topology.TopologyValidationResponseBean;
import com.heal.dashboard.service.businesslogic.topology.ServiceDetailsBL;
import com.heal.dashboard.service.businesslogic.topology.TopologyServiceBL;
import com.heal.dashboard.service.entities.AccountBean;
import com.heal.dashboard.service.entities.ApplicationDetailBean;
import com.heal.dashboard.service.entities.ApplicationResponseBean;
import com.heal.dashboard.service.entities.UserAccessAccountsBean;
import com.heal.dashboard.service.entities.UtilityBean;
import com.heal.dashboard.service.exception.ClientException;
import com.heal.dashboard.service.exception.CustomExceptionHandler;
import com.heal.dashboard.service.exception.DataProcessingException;
import com.heal.dashboard.service.exception.ServerException;
import com.heal.dashboard.service.pojo.RequestObject;
import com.heal.dashboard.service.util.Constants;

@Service
public class TopologyDetailsService {

	@Autowired
	ServiceDetailsBL serviceDetailsBL;
	@Autowired
	TopologyServiceBL  topologyServiceBL;
	
	
	 public  TopologyDetails getServiceDetails(String authorizationToken,String identifier,String serviceId,String ndegree) {
		 RequestObject<String> requestObject = new RequestObject<String>();
	        requestObject.addHeaders(Constants.AUTHORIZATION_TOKEN, authorizationToken);
	        Map<String,String> params = new HashMap<String,String>();
	        params.put("identifier", identifier);
	        params.put("serviceId", serviceId);
	        params.put("ndegree", ndegree);
	        requestObject.setParams(params);
	        
	        TopologyDetails topologyDetailsResponse;
	        try {
	            UtilityBean<String> utilityBean = serviceDetailsBL.clientValidation(requestObject);
	            TopologyValidationResponseBean topologyValidationResponseBean = serviceDetailsBL.serverValidation(utilityBean);
	            topologyDetailsResponse = serviceDetailsBL.process(topologyValidationResponseBean);
	        } catch (ServerException | DataProcessingException | ClientException  e) {
				throw new CustomExceptionHandler(e.getMessage());
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
	        return topologyDetailsResponse;
		 
	
	 }
	 
	 
	 public  TopologyDetails getTopologyDetails(String authorizationToken,String identifier, String applicationId) {
		 RequestObject<String> requestObject = new RequestObject<String>();
	        requestObject.addHeaders(Constants.AUTHORIZATION_TOKEN, authorizationToken);
	        Map<String,String> params = new HashMap<String,String>();
	        params.put("identifier", identifier);
	        requestObject.setParams(params);
	        requestObject.addQueryParam("applicationId", applicationId);
	        
	        TopologyDetails topologyDetailsResponse;
	        try {
	            UtilityBean<String> utilityBean = topologyServiceBL.clientValidation(requestObject);
	            TopologyValidationResponseBean topologyValidationResponseBean = topologyServiceBL.serverValidation(utilityBean);
	            topologyDetailsResponse = topologyServiceBL.process(topologyValidationResponseBean);
	        } catch (ServerException | DataProcessingException | ClientException  e) {
				throw new CustomExceptionHandler(e.getMessage());
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
	        return topologyDetailsResponse;
		 
	
	 }
	 
	 
	 

	
	
}

