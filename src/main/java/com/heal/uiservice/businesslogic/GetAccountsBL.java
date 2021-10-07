package com.heal.uiservice.businesslogic;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.heal.uiservice.dao.mysql.JDBCTemplateDao;
import com.heal.uiservice.entities.*;
import com.heal.uiservice.exception.ClientException;
import com.heal.uiservice.exception.DataProcessingException;
import com.heal.uiservice.exception.ServerException;
import com.heal.uiservice.pojo.RequestObject;
import com.heal.uiservice.util.Constants;
import com.heal.uiservice.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GetAccountsBL implements BusinessLogic<String, UserAccessAccountsBean, List<AccountBean>> {


    @Autowired
    JDBCTemplateDao jdbcTemplateDao;

    @Override
    public UtilityBean<String> clientValidation(RequestObject requestObject) throws ClientException {
        String jwtToken = requestObject.getHeaders().get(Constants.AUTHORIZATION_TOKEN);
        if (null == jwtToken || jwtToken.trim().isEmpty()) {
            throw new ClientException(Constants.AUTHORIZATION_TOKEN_IS_NULL_OR_EMPTY);
        }

        String userId = Utility.extractUserIdFromJWT(jwtToken);
        if(null == userId || userId.trim().isEmpty()) {
            throw new ClientException("User details extraction failure");
        }

        return UtilityBean.<String>builder()
                .pojoObject(Utility.extractUserIdFromJWT(jwtToken))
                .build();
    }

    @Override
    public UserAccessAccountsBean serverValidation(UtilityBean<String> utilityBean) throws ServerException {
        String userId = utilityBean.getPojoObject();

        UserAccessDetails userAccessDetails;
        try {
            userAccessDetails = jdbcTemplateDao.fetchUserAccessDetailsUsingIdentifier(userId);
            if (null == userAccessDetails || null == userAccessDetails.getAccessDetails()) {
                log.error("Invalid user access details. Details: Required access details for user [{}] is unavailable", userId);
                throw new ServerException("Invalid user access details");
            }
        } catch (Exception e) {
            log.error("Error while fetching user access information for user [{}]. Details: ", userId, e);
            throw new ServerException("Error while fetching user access information");
        }

        AccountMappingBean accountMappingBean;
        try {
            accountMappingBean = new Gson().fromJson(userAccessDetails.getAccessDetails(), AccountMappingBean.class);
        } catch (JsonSyntaxException exception) {
            log.error("Invalid user account mapping information. Details: ", exception);
            throw new ServerException("User access details JSON to POJO conversion failure");
        }


        List<AccountBean> accessibleAccounts = jdbcTemplateDao.getAccountDetails(Constants.TIME_ZONE_TAG, Constants.ACCOUNT_TABLE_NAME_MYSQL_DEFAULT);
        if(null == accessibleAccounts) {
            log.error("Account information unavailable");
            throw new ServerException("Account information unavailable");
        }

        return new UserAccessAccountsBean(accountMappingBean, accessibleAccounts);
    }

    @Override
    public List<AccountBean> process(UserAccessAccountsBean bean) throws DataProcessingException {
        AccountMappingBean accountMappingBean = bean.getAccountMappingBean();
        List<AccountBean> accounts = bean.getAccounts();
        List<String> accountIdentifiers = accountMappingBean.getAccounts();

        if(!accountIdentifiers.contains("*")) {
            accounts = accounts.parallelStream()
                    .filter(acc -> accountIdentifiers.contains(acc.getIdentifier()))
                    .collect(Collectors.toList());
        }

        return accounts;
    }
}
