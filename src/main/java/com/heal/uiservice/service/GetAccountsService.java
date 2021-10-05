package com.heal.uiservice.service;

import com.heal.uiservice.businesslogic.GetAccountsBL;
import com.heal.uiservice.entities.AccountBean;
import com.heal.uiservice.entities.UserAccessAccountsBean;
import com.heal.uiservice.entities.UtilityBean;
import com.heal.uiservice.exception.ClientException;
import com.heal.uiservice.exception.DataProcessingException;
import com.heal.uiservice.exception.ServerException;
import com.heal.uiservice.pojo.RequestObject;
import com.heal.uiservice.pojo.ResponseBean;
import com.heal.uiservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetAccountsService {
    @Autowired
    GetAccountsBL getAccountsBL;

    public ResponseBean<List<AccountBean>> getAccountList(String authorizationToken) {
        RequestObject requestObject = new RequestObject();
        requestObject.addHeaders(Constants.AUTHORIZATION_TOKEN, authorizationToken);
        try {
            UtilityBean<String> userId = getAccountsBL.clientValidation(requestObject);
            UserAccessAccountsBean bean = getAccountsBL.serverValidation(userId);
            List<AccountBean> accounts = getAccountsBL.process(bean);

            return new ResponseBean<>("", accounts, HttpStatus.OK);
        } catch (ClientException | ServerException | DataProcessingException e) {
            e.printStackTrace();
            return new ResponseBean<>("", null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean<>("", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
