package com.heal.dashboard.service.businesslogic.account;



import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.heal.dashboard.service.businesslogic.BusinessLogic;
import com.heal.dashboard.service.dao.mysql.AccountDao;
import com.heal.dashboard.service.entities.AccountBean;
import com.heal.dashboard.service.entities.AccountMappingBean;
import com.heal.dashboard.service.entities.UserAccessAccountsBean;
import com.heal.dashboard.service.entities.UserAccessBean;
import com.heal.dashboard.service.entities.UtilityBean;
import com.heal.dashboard.service.exception.ClientException;
import com.heal.dashboard.service.exception.DataProcessingException;
import com.heal.dashboard.service.exception.ServerException;
import com.heal.dashboard.service.pojo.RequestObject;
import com.heal.dashboard.service.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GetAccountsBL implements BusinessLogic<String, UserAccessAccountsBean, List<AccountBean>> {


    @Autowired
    AccountDao accountdao;

    @Override
    public UtilityBean<String> clientValidation(RequestObject<String> requestObject) throws ClientException {
        String jwtToken = requestObject.getHeaders().get(Constants.AUTHORIZATION_TOKEN);
        if (null == jwtToken || jwtToken.trim().isEmpty()) {
            throw new ClientException(Constants.AUTHORIZATION_TOKEN_IS_NULL_OR_EMPTY);
        }

       // String userId = Utility.extractUserIdFromJWT(jwtToken);
       String userId =jwtToken;
        if(null == userId || userId.trim().isEmpty()) {
            throw new ClientException("User details extraction failure");
        }
        return UtilityBean.<String>builder()
        		 .pojoObject(userId)
                .build();
    }

    @Override
    public UserAccessAccountsBean serverValidation(UtilityBean<String> utilityBean) throws ServerException {
        String userId = utilityBean.getPojoObject();

        	UserAccessBean  userAccessDetails = accountdao.fetchUserAccessDetailsUsingIdentifier(userId);
            if (null == userAccessDetails || null == userAccessDetails.getAccessDetails()) {
                log.error("Invalid user access details. Details: Required access details for user [{}] is unavailable", userId);
                throw new ServerException("Invalid user access details");
            }

        AccountMappingBean accountMappingBean;
        try {
            accountMappingBean = new Gson().fromJson(userAccessDetails.getAccessDetails(), AccountMappingBean.class);
        } catch (JsonSyntaxException exception) {
            log.error("Invalid user account mapping information. Details: ", exception);
            throw new ServerException("User access details JSON to POJO conversion failure");
        }

        List<AccountBean> accessibleAccounts = accountdao.getAccountDetails(Constants.TIME_ZONE_TAG, Constants.ACCOUNT_TABLE_NAME_MYSQL_DEFAULT);
        if(null == accessibleAccounts) {
            log.error("Account information unavailable");
            throw new ServerException("Account information unavailable");
        }

        return new UserAccessAccountsBean(accountMappingBean, accessibleAccounts);
    }

    @Override
    public List<AccountBean> process(UserAccessAccountsBean bean) throws DataProcessingException {
        AccountMappingBean accountMappingBean = bean.getAccountMappingBean();
        List<AccountBean> accounts	 = bean.getAccounts();
        List<String> accountIdentifiers = accountMappingBean.getAccounts();	

        if(!accountIdentifiers.contains("*")) {
            accounts = accounts.parallelStream()
                    .filter(acc -> accountIdentifiers.contains(acc.getIdentifier()))
                    .collect(Collectors.toList());
        }

        return accounts;
    }
}
