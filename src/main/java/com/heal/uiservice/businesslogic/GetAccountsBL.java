package com.heal.uiservice.businesslogic;



import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.heal.uiservice.dao.mysql.AccountDao;
import com.heal.uiservice.entities.AccountBean;
import com.heal.uiservice.entities.AccountMappingBean;
import com.heal.uiservice.entities.UserAccessAccountsBean;
import com.heal.uiservice.entities.UserAccessDetails;
import com.heal.uiservice.entities.UtilityBean;
import com.heal.uiservice.exception.ClientException;
import com.heal.uiservice.exception.DataProcessingException;
import com.heal.uiservice.exception.ServerException;
import com.heal.uiservice.pojo.RequestObject;
import com.heal.uiservice.util.Constants;

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
        String userId = jwtToken;
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

        	UserAccessDetails  userAccessDetails = accountdao.fetchUserAccessDetailsUsingIdentifier(userId);
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
