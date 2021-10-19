package com.heal.uiservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heal.uiservice.businesslogic.GetAccountsBL;
import com.heal.uiservice.entities.AccountBean;
import com.heal.uiservice.entities.UserAccessAccountsBean;
import com.heal.uiservice.entities.UtilityBean;
import com.heal.uiservice.exception.ClientException;
import com.heal.uiservice.exception.DataProcessingException;
import com.heal.uiservice.exception.CustomExceptionHandler;
import com.heal.uiservice.exception.ServerException;
import com.heal.uiservice.pojo.RequestObject;
import com.heal.uiservice.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GetAccountsService {
    @Autowired
    GetAccountsBL getAccountsBL;

    public List<AccountBean> getAccountList(String authorizationToken) {
        RequestObject<String> requestObject = new RequestObject<String>();
        requestObject.addHeaders(Constants.AUTHORIZATION_TOKEN, authorizationToken);
        List<AccountBean> accounts;
        try {
            UtilityBean<String> utilityBean = getAccountsBL.clientValidation(requestObject);
            UserAccessAccountsBean userAccessBean = getAccountsBL.serverValidation(utilityBean);
            accounts = getAccountsBL.process(userAccessBean);
        } catch (ServerException | DataProcessingException | ClientException  e) {
			throw new CustomExceptionHandler(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
        return accounts;
    }
}

