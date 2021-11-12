package com.heal.dashboard.service.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.heal.dashboard.service.businesslogic.account.ApplicationBL;
import com.heal.dashboard.service.businesslogic.account.ApplicationHealthBL;
import com.heal.dashboard.service.businesslogic.account.DateComponentBL;
import com.heal.dashboard.service.businesslogic.account.GetAccountsBL;
import com.heal.dashboard.service.businesslogic.account.MasterFeaturesBL;
import com.heal.dashboard.service.entities.AccountBean;
import com.heal.dashboard.service.entities.ApplicationDetailBean;
import com.heal.dashboard.service.entities.ApplicationHealthResponse;
import com.heal.dashboard.service.entities.ApplicationResponseBean;
import com.heal.dashboard.service.entities.DateComponentBean;
import com.heal.dashboard.service.entities.DateComponentDetailBean;
import com.heal.dashboard.service.entities.MasterFeatureDetails;
import com.heal.dashboard.service.entities.MasterFeaturesBean;
import com.heal.dashboard.service.entities.UserAccessAccountsBean;
import com.heal.dashboard.service.entities.UtilityBean;
import com.heal.dashboard.service.entities.applicationhealth.ApplicationHealthDetail;
import com.heal.dashboard.service.exception.ClientException;
import com.heal.dashboard.service.exception.CustomExceptionHandler;
import com.heal.dashboard.service.exception.DataProcessingException;
import com.heal.dashboard.service.exception.ServerException;
import com.heal.dashboard.service.pojo.RequestObject;
import com.heal.dashboard.service.util.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GetAccountsService {
    @Autowired
    GetAccountsBL getAccountsBL;
    @Autowired
    ApplicationBL applicationBL;
    @Autowired
    MasterFeaturesBL masterFeaturesBL;
    @Autowired
    DateComponentBL dateComponentBL;
    @Autowired
    ApplicationHealthBL applicationHealthBL;

    public List<AccountBean> getAccountList(String authorizationToken) {
        RequestObject<String> requestObject = new RequestObject<String>();
        requestObject.addHeaders(Constants.AUTHORIZATION_TOKEN, authorizationToken);
        List<AccountBean> accounts;
        try {
            UtilityBean<String> utilityBean = getAccountsBL.clientValidation(requestObject);
            UserAccessAccountsBean userAccessBean = getAccountsBL.serverValidation(utilityBean);
            accounts = getAccountsBL.process(userAccessBean);
        } catch (ServerException | DataProcessingException | ClientException e) {
			throw new CustomExceptionHandler(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
        return accounts;
    }
    
    public List<ApplicationDetailBean> getApplicationList(String authorizationToken,String identifier) {
        RequestObject<String> requestObject = new RequestObject<String>();
        requestObject.addHeaders(Constants.AUTHORIZATION_TOKEN, authorizationToken);
        requestObject.setBody(identifier);
        List<ApplicationDetailBean> applicationDetailBean;
        try {
            UtilityBean<String> utilityBean = applicationBL.clientValidation(requestObject);
            ApplicationResponseBean applicationResponseBean = applicationBL.serverValidation(utilityBean);
            applicationDetailBean = applicationBL.process(applicationResponseBean);
        } catch (ServerException | DataProcessingException | ClientException  e) {
			throw new CustomExceptionHandler(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
        return applicationDetailBean;
    }
    
    public MasterFeatureDetails getMasterFeatures() {
        RequestObject<String> requestObject = new RequestObject<>();
        MasterFeatureDetails response = null;
        try{
            UtilityBean<String> utilityBean = masterFeaturesBL.clientValidation(requestObject);
            List<MasterFeaturesBean> masterFeaturesBeans =masterFeaturesBL.serverValidation(utilityBean);
            response = masterFeaturesBL.process(masterFeaturesBeans);
        } catch (ServerException | DataProcessingException | ClientException  e) {
			throw new CustomExceptionHandler(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
        return response;
    }

    public DateComponentDetailBean getDateTimeDropdownList() {
        RequestObject<String> requestObject = new RequestObject<>();
        DateComponentDetailBean response = null;
        try{
            UtilityBean<String> utilityBean = dateComponentBL.clientValidation(requestObject);
            List<DateComponentBean> dateComponentBeans =dateComponentBL.serverValidation(utilityBean);
            response = dateComponentBL.process(dateComponentBeans);
        } catch (ServerException | DataProcessingException | ClientException  e) {
			throw new CustomExceptionHandler(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
        return response;
    }

    public List<ApplicationHealthDetail>  getApplicationHealthStatus(String identifier, String toTimeString,String authorizationToken) {
       RequestObject<String> requestObject = new RequestObject<>();
        Map<String, String> params = new HashMap<String,String>();
        params.put("identifier", identifier);
        params.put("toTime", toTimeString);
        requestObject.setParams(params);
        requestObject.addHeaders(Constants.AUTHORIZATION_TOKEN, authorizationToken);

 
        List<ApplicationHealthDetail>  response = null;
        try{
            UtilityBean<String> utilityBean = applicationHealthBL.clientValidation(requestObject);
            ApplicationHealthResponse applicationResponseBean = applicationHealthBL.serverValidation(utilityBean);
            response = applicationHealthBL.process(applicationResponseBean);
        } catch (ServerException | DataProcessingException | ClientException  e) {
			throw new CustomExceptionHandler(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
        return response;
    }

}

