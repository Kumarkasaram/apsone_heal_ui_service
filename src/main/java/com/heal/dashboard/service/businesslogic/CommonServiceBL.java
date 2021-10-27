package com.heal.dashboard.service.businesslogic;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.heal.dashboard.service.dao.mysql.AccountDao;
import com.heal.dashboard.service.dao.mysql.ControllerDao;
import com.heal.dashboard.service.dao.mysql.TagsDao;
import com.heal.dashboard.service.dao.mysql.TransactionDao;
import com.heal.dashboard.service.dao.mysql.MasterDataDao;
import com.heal.dashboard.service.entities.AccountBean;
import com.heal.dashboard.service.entities.AccountMappingBean;
import com.heal.dashboard.service.entities.AccountServiceKey;
import com.heal.dashboard.service.entities.ApplicationBean;
import com.heal.dashboard.service.entities.Controller;
import com.heal.dashboard.service.entities.TagDetails;
import com.heal.dashboard.service.entities.TagMapping;
import com.heal.dashboard.service.entities.TxnAndGroupBean;
import com.heal.dashboard.service.entities.UserAccessDetails;
import com.heal.dashboard.service.entities.ViewApplicationServiceMappingBean;
import com.heal.dashboard.service.entities.ViewTypeBean;
import com.heal.dashboard.service.exception.ServerException;
import com.heal.dashboard.service.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CommonServiceBL {

	@Autowired
	private  AccountDao accountDao;
	@Autowired
	private  ControllerDao controllerDao;
	@Autowired
	TagsDao tagsDao;
	@Autowired
	MasterDataDao MasterDataDao;
	@Autowired
	TransactionDao transactionDao;

	
	
	private  ViewTypeBean viewTypeBean= null;
	
	 public  UserAccessDetails extractUserAccessDetails(String accessDetails,AccountBean account) throws ServerException {
	        UserAccessDetails userAccessDetails = null;

	        AccountMappingBean   accountMappingBean = new Gson().fromJson(accessDetails, AccountMappingBean.class);

	        if (accountMappingBean != null && accountMappingBean.getAccounts() != null) {
	            if (accountMappingBean.getAccounts().contains(account.getIdentifier())) {
	                int accessibleAccountId = account.getId();

	                Map<String,List<String>> accessibleApplications = accountMappingBean.getAccountMapping();

	                if (accessibleApplications == null || accessibleApplications.isEmpty()) {
	                    log.error("There no applications mapped to account [{}] and user [{}]");
	                   
	                    return null;
	                }

	                ApplicationBean applicationIdentifiers = (ApplicationBean) accessibleApplications.get(account.getIdentifier());
	                List<Controller> applicationControllerList = getControllersByType(Constants.APPLICATION_CONTROLLER_TYPE, accessibleAccountId);

	                if (applicationIdentifiers.getApplications().contains("*")) {
	                    userAccessDetails = buildUserAccessDetails(accessibleAccountId, applicationControllerList);
	                } else {
	                    applicationControllerList = applicationControllerList.parallelStream()
	                            .filter(app -> (applicationIdentifiers.getApplications().contains(app.getIdentifier())))
	                            .collect(Collectors.toList());

	                    userAccessDetails = buildUserAccessDetails(accessibleAccountId, applicationControllerList);
	                }
	            } else if (accountMappingBean.getAccounts().contains("*")) {
	                int accessibleAccountId =account.getId();

	                List<Controller> applicationControllerList = getControllersByType(Constants.APPLICATION_CONTROLLER_TYPE, accessibleAccountId);

	                userAccessDetails = buildUserAccessDetails(accessibleAccountId, applicationControllerList);
	            }
	        }

	        return userAccessDetails;
	    }
	 
	 
	 
	 public   List<Controller> getControllersByType(String serviceType, int accountId) throws ServerException {
	        Optional<ViewTypeBean> subTypeOptional = null; 
	           // get the service mst sub type
	            List<ViewTypeBean> subTypeBean = MasterDataDao.getAllViewTypes();
	            if(subTypeBean!=null && subTypeBean.size()>0) {
	            	 subTypeOptional = subTypeBean
	                         .stream()
	                         .filter(it -> (Constants.CONTROLLER_TYPE.trim().equalsIgnoreCase(it.getTypeName())))
	                         .filter(it -> (serviceType.trim().equalsIgnoreCase(it.getSubTypeName())))
	                         .findAny();
	            }
	            
	            //get the all app for accountId
	            List<Controller> controllerList  = controllerDao.getControllerList(accountId);
	            //filter with controller_type_id
	            if(controllerList!=null && controllerList.size()>0 && subTypeOptional.isPresent()) {
	            	viewTypeBean = subTypeOptional.get();
	            	controllerList = controllerList.stream()
	 	                  .filter(t -> t.getControllerTypeId() == viewTypeBean.getSubTypeId())
	 	                 .collect(Collectors.toList());
	            }
	            
	            return controllerList;

	    }
	 
	 
	 private  UserAccessDetails buildUserAccessDetails(int accountId, List<Controller> applicationControllerList) throws ServerException {
	        UserAccessDetails userAccessDetails = new UserAccessDetails();


	        Map<String, List<ViewApplicationServiceMappingBean>> appIdentifierMap = controllerDao.getApplicationServicesByAccount(accountId)
	                .stream()
	                .collect(Collectors.groupingBy(ViewApplicationServiceMappingBean::getApplicationIdentifier));


	        List<ViewApplicationServiceMappingBean> appServiceDetails = applicationControllerList.parallelStream()
	                .map(c -> appIdentifierMap.getOrDefault(c.getIdentifier(), null))
	                .filter(Objects::nonNull)
	                .flatMap(Collection::stream)
	                .collect(Collectors.toList());

	        userAccessDetails.setApplicationIdentifiers(appServiceDetails.parallelStream().map(ViewApplicationServiceMappingBean::getApplicationIdentifier)
	                .distinct().collect(Collectors.toList()));

	        List<Integer> appIds = appServiceDetails.parallelStream().map(ViewApplicationServiceMappingBean::getApplicationId)
	                .distinct().collect(Collectors.toList());
	        userAccessDetails.setApplicationIds(appIds);

	        if (appServiceDetails.isEmpty()) {
	            userAccessDetails.setApplicationIds(new ArrayList<>());
	            userAccessDetails.setServiceIds(new ArrayList<>());
	            userAccessDetails.setTransactionIds(new ArrayList<>());
	            userAccessDetails.setServiceIdentifiers(new ArrayList<>());

	            return userAccessDetails;
	        }

	        List<Integer> serviceIds = appServiceDetails.parallelStream().map(ViewApplicationServiceMappingBean::getServiceId)
	                .distinct().collect(Collectors.toList());
	        if (serviceIds.isEmpty()) {
	            userAccessDetails.setServiceIds(new ArrayList<>());
	            userAccessDetails.setTransactionIds(new ArrayList<>());
	            userAccessDetails.setServiceIdentifiers(new ArrayList<>());

	            return userAccessDetails;
	        }
	        userAccessDetails.setServiceIds(serviceIds);

	        userAccessDetails.setServiceIdentifiers(appServiceDetails.parallelStream().map(ViewApplicationServiceMappingBean::getServiceIdentifier)
	                .distinct().collect(Collectors.toList()));

	        List<Integer> transactionIds = accountDao.getTransactionsIdsForAccount(accountId);
	        userAccessDetails.setTransactionIds(transactionIds);

	        return userAccessDetails;
	    }
	
	 
	 public  List<TagMapping> getServiceTags(int serviceId, int accountId) throws ServerException {
	        long st = System.currentTimeMillis();
	        TagDetails layerTag = tagsDao.getTagDetails(Constants.LAYER_TAG, Constants.DEFAULT_ACCOUNT_ID);
	        List<TagMapping> serviceTags = tagsDao.getTagMappingDetails(layerTag.getId(), serviceId, Constants.CONTROLLER, accountId);

	        TagDetails entrypointTag = tagsDao.getTagDetails(Constants.ENTRY_POINT, Constants.DEFAULT_ACCOUNT_ID);
	        serviceTags.addAll(tagsDao.getTagMappingDetails(entrypointTag.getId(), serviceId, Constants.CONTROLLER, accountId));

	        TagDetails serviceTypeTag = tagsDao.getTagDetails(Constants.SERVICE_TYPE_TAG, Constants.DEFAULT_ACCOUNT_ID);
	        serviceTags.addAll(tagsDao.getTagMappingDetails(serviceTypeTag.getId(), serviceId, Constants.CONTROLLER, accountId));

	        serviceTags.addAll(tagsDao.getTagMappingDetailsByTagKey(String.valueOf(serviceId), Constants.CONTROLLER, accountId));
	        serviceTags.addAll(tagsDao.getTagMappingDetailsByTagKey(String.valueOf(serviceId), Constants.AGENT_TABLE, accountId));
	        log.info("Time take for getting service tags is {} ms. serviceId:{}, accountId:{}", (System.currentTimeMillis() - st), serviceId, accountId);
	        return serviceTags;
	    }

		public  List<TxnAndGroupBean> getTransactionForApplication(int accountId) throws ServerException {
	        long start = System.currentTimeMillis();
	        List<TxnAndGroupBean> txnList = transactionDao.getTxnAndGroupPerService(accountId);
	        log.debug("Time taken to fetch txn's for acc: {} ,service: {} is {} ms.", accountId,
	                (System.currentTimeMillis() - start));
	        return txnList;
}
	 
}

