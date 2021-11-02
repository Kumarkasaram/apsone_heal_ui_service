package com.heal.dashboard.service.businesslogic.account;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.heal.dashboard.service.businesslogic.BusinessLogic;
import com.heal.dashboard.service.businesslogic.CommonServiceBL;
import com.heal.dashboard.service.dao.mysql.AccountDao;
import com.heal.dashboard.service.entities.AccountBean;
import com.heal.dashboard.service.entities.ApplicationDetailBean;
import com.heal.dashboard.service.entities.ApplicationResponseBean;
import com.heal.dashboard.service.entities.Controller;
import com.heal.dashboard.service.entities.TxnAndGroupBean;
import com.heal.dashboard.service.entities.UserAccessBean;
import com.heal.dashboard.service.entities.UserAccessDetails;
import com.heal.dashboard.service.entities.UtilityBean;
import com.heal.dashboard.service.entities.ViewTypeBean;
import com.heal.dashboard.service.exception.ClientException;
import com.heal.dashboard.service.exception.DataProcessingException;
import com.heal.dashboard.service.exception.ServerException;
import com.heal.dashboard.service.pojo.RequestObject;
import com.heal.dashboard.service.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicationBL implements BusinessLogic<String, ApplicationResponseBean, List<ApplicationDetailBean>> {

	 @Autowired
	 AccountDao accountdao;
	 private ViewTypeBean viewTypeBean= null;
	 @Autowired
	 CommonServiceBL commonServiceBL;
	 
	@Override
	public UtilityBean<String> clientValidation(RequestObject<String> requestObject) throws ClientException {
		   String jwtToken = requestObject.getHeaders().get(Constants.AUTHORIZATION_TOKEN);
	        if (null == jwtToken || jwtToken.trim().isEmpty()) {
	            throw new ClientException(Constants.AUTHORIZATION_TOKEN_IS_NULL_OR_EMPTY);
	        }
	        String identifier = requestObject.getBody();
	        if (null == identifier || identifier.trim().isEmpty()) {
	            throw new ClientException("identifier cant be null or empty");
	        }

	       // String userId = Utility.extractUserIdFromJWT(jwtToken);
	        String userId = jwtToken;
	        if(null == userId || userId.trim().isEmpty()) {
	            throw new ClientException("User details extraction failure");
	        }
	        return UtilityBean.<String>builder()
	        		 .authToken(userId)
	        		 .accountIdentifier(identifier)
	                .build();
	}

	
	@Override
	public ApplicationResponseBean serverValidation(UtilityBean<String> utilityBean)
			throws ServerException {
		AccountBean accountBean =null;
		UserAccessDetails userAccessDetails =null;
		List<AccountBean> accountsList = accountdao.getAccountDetails(Constants.TIME_ZONE_TAG, Constants.ACCOUNT_TABLE_NAME_MYSQL_DEFAULT);
		 if(accountsList != null || !accountsList.isEmpty()) {
	        Optional<AccountBean> accountOptional = accountsList
	                .stream()
	                .filter( it -> it.getIdentifier().equals(utilityBean.getAccountIdentifier()))
	                .findAny();
	        if(accountOptional.isPresent()) {
	        	accountBean = accountOptional.get();
	        } else {
	        	log.error("Error while fetching account detail information for  Details: ", utilityBean.getAccountIdentifier());
				throw new ServerException("\"Invalid input parameter/s provided. Details:, UserIdentifier :"+utilityBean.getAccountIdentifier());
	        }
		 }
	    
		UserAccessBean userAccessBean = accountdao.fetchUserAccessDetailsUsingIdentifier(utilityBean.getAuthToken());
		if(userAccessBean !=null) {
                 userAccessDetails = commonServiceBL.extractUserAccessDetails(userAccessBean.getAccessDetails(),accountBean);
                 if(userAccessDetails == null) {
     	        	log.error("Exception occur while extractUserAccessDetails");
     				throw new ServerException("Exception occur while extractUserAccessDetails");
                 }
		}
    	List<TxnAndGroupBean> appTxnDetails =commonServiceBL.getTransactionForApplication(accountBean.getId());
        List<Controller>  controllerBeanList =  commonServiceBL.getControllersByType(Constants.APPLICATION_CONTROLLER_TYPE,accountBean.getId());  
        return new ApplicationResponseBean(accountBean.getId(),controllerBeanList,appTxnDetails,userAccessDetails);
        
	}

	@Override
	public List<ApplicationDetailBean> process(ApplicationResponseBean applicationResponseBean) throws DataProcessingException {
				List<ApplicationDetailBean> applicationDetailBeanList = new ArrayList<>();
	            List<String> userApps = applicationResponseBean.getUserAccessDetails().getApplicationIdentifiers();

	            if (userApps.isEmpty()) {
     	        		log.error("there is no application map with user");
	                	throw new DataProcessingException("there is no application map with user");
	                }
	            List<Controller> applicationList =  applicationResponseBean.getControllerBeanList();
	            applicationList = applicationList.stream()
	                        .filter(it -> userApps.contains(it.getIdentifier()))
	                        .collect(Collectors.toList());
	            if (applicationList != null && !applicationList.isEmpty()) {
	            	
	            	List<TxnAndGroupBean>appTxnDetails = applicationList.stream().flatMap(obj->applicationResponseBean.getAppTxnDetails().stream().filter(it->it.getServiceId().equalsIgnoreCase(obj.getAppId()))).collect(Collectors.toList());
	            	ApplicationDetailBean applicationDetailBean;
	            	for(Controller application : applicationList) {
	            		applicationDetailBean = new ApplicationDetailBean();
	            		applicationDetailBean.setId(Integer.parseInt(application.getAppId()));
	            		applicationDetailBean.setName(application.getName());
	            		applicationDetailBean.setIdentifier(application.getIdentifier());
	            		if( appTxnDetails != null && ! appTxnDetails.isEmpty()) {
	            			boolean hasTransactionConfigured = appTxnDetails.stream().anyMatch( it -> ( it.getIsBusinessTransaction() == 1));
	            			applicationDetailBean.setHasTransactionConfigured(hasTransactionConfigured);
	            		}
	            		applicationDetailBeanList.add(applicationDetailBean);
	            			
	            	}
	            		
	            	}
	            	
	            applicationDetailBeanList.sort( java.util.Comparator.comparing( ApplicationDetailBean::isHasTransactionConfigured, java.util.Comparator.reverseOrder()).
	                    thenComparing( ApplicationDetailBean::getName ));



		return applicationDetailBeanList;
	}
	
	
}
