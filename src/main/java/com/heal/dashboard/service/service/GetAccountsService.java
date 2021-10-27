package com.heal.dashboard.service.service;

import java.util.List;

import com.heal.dashboard.service.businesslogic.account.GetAccountsBL;
import com.heal.dashboard.service.entities.AccountBean;
import com.heal.dashboard.service.entities.UserAccessAccountsBean;
import com.heal.dashboard.service.entities.UtilityBean;
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
}

