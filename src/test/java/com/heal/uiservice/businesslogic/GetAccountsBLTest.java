package com.heal.uiservice.businesslogic;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.heal.uiservice.businesslogic.GetAccountsBL;
import com.heal.uiservice.dao.mysql.AccountDao;
import com.heal.uiservice.entities.AccountBean;
import com.heal.uiservice.entities.AccountMappingBean;
import com.heal.uiservice.entities.UserAccessAccountsBean;
import com.heal.uiservice.entities.UserAccessDetails;
import com.heal.uiservice.entities.UtilityBean;
import com.heal.uiservice.exception.ClientException;
import com.heal.uiservice.exception.ServerException;
import com.heal.uiservice.pojo.RequestObject;
import com.heal.uiservice.util.Constants;

@RunWith(SpringRunner.class)
public class GetAccountsBLTest {

	@InjectMocks
    GetAccountsBL  getAccountBL;
    @Mock
    AccountDao jdbcTemplateDao;
   
    @Mock
    List<AccountBean> accountBeansList;
    @Mock
    UserAccessDetails userAccessDetails;
    
    UserAccessAccountsBean accessAccountsBean;
    RequestObject<String> requestObject;
    
    @Before
    public void setup() {
//      setting up mock data in  accountBean
        accountBeansList = new ArrayList<>();
        AccountBean  accountBean = new AccountBean();
        accountBean.setAccountId(2);
        accountBean.setIdentifier("qa-d681ef13-d690-4917-jkhg-6c79b-1");
        accountBean.setName("India");
        accountBeansList.add(accountBean);

//      setting up mock data in  userAccessDetails
        userAccessDetails = new UserAccessDetails();
        userAccessDetails.setAccessDetails("{\"accounts\": [\"*\"]}");
        userAccessDetails.setId(1);
        userAccessDetails.setUpdatedTime(LocalDateTime.now());
        userAccessDetails.setCreatedTime(LocalDateTime.now());
        userAccessDetails.setUserIdentifier("7640123a-fbde-4fe5-9812-581cd1e3a9c1");
        accessAccountsBean = new UserAccessAccountsBean(new Gson().fromJson(userAccessDetails.getAccessDetails(), AccountMappingBean.class), accountBeansList);
    	
    
    	
    }	

    @Test
    public void getClientValidation_Success() throws Exception {
        requestObject = new RequestObject<String>();
        requestObject.addHeaders(Constants.AUTHORIZATION_TOKEN, "7640123a-fbde-4fe5-9812-581cd1e3a9c1");
        Assert.assertEquals("7640123a-fbde-4fe5-9812-581cd1e3a9c1", getAccountBL.clientValidation(requestObject).getPojoObject());
    }
    @Test(expected = ClientException.class)  
    public void getClientValidation_ClientException1() throws ClientException {
        requestObject = new RequestObject<String>();
        requestObject.addHeaders(Constants.AUTHORIZATION_TOKEN, null);
        getAccountBL.clientValidation(requestObject);
      
    }

    @Test
    public void serverValidation() throws Exception {
    	 UtilityBean<String> utilityBean = UtilityBean.<String>builder().pojoObject("7640123a-fbde-4fe5-9812-581cd1e3a9c1").build();
           Mockito.when(jdbcTemplateDao.fetchUserAccessDetailsUsingIdentifier(Mockito.anyString())).thenReturn(userAccessDetails);
           Mockito.when(jdbcTemplateDao.getAccountDetails(Mockito.anyString(), Mockito.anyString())).thenReturn(accountBeansList);
        Assert.assertEquals(accountBeansList.get(0).getIdentifier(), getAccountBL.serverValidation(utilityBean).getAccounts().get(0).getIdentifier());
    }

    @Test(expected = ServerException.class )
    public void serverValidation_Case2() throws Exception {
    	 UtilityBean<String> utilityBean = UtilityBean.<String>builder().pojoObject("7640123a-fbde-4fe5-9812-581cd1e3a9c1").build();
           Mockito.when(jdbcTemplateDao.fetchUserAccessDetailsUsingIdentifier(Mockito.anyString())).thenReturn(null);
           Mockito.when(jdbcTemplateDao.getAccountDetails(Mockito.anyString(), Mockito.anyString())).thenReturn(accountBeansList);
           Assert.assertEquals(accountBeansList.get(0).getIdentifier(), getAccountBL.serverValidation(utilityBean).getAccounts().get(0).getIdentifier());
    }

    @Test(expected = ServerException.class )
    public void serverValidation_Case3() throws Exception {
    	 UtilityBean<String> utilityBean = UtilityBean.<String>builder().pojoObject("7640123a-fbde-4fe5-9812-581cd1e3a9c1").build();
           Mockito.when(jdbcTemplateDao.fetchUserAccessDetailsUsingIdentifier(Mockito.anyString())).thenReturn(userAccessDetails);
           Mockito.when(jdbcTemplateDao.getAccountDetails(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
           Assert.assertEquals(accountBeansList.get(0).getIdentifier(), getAccountBL.serverValidation(utilityBean).getAccounts().get(0).getIdentifier());
    }

    @Test
    public void processData() throws Exception {
           Assert.assertEquals(accountBeansList.get(0).getIdentifier(), getAccountBL.process(accessAccountsBean).get(0).getIdentifier());
    }
    @Test
    public void processData_Case2() throws Exception {
    		userAccessDetails.setAccessDetails("{\"accounts\": [\"qa-d681ef13-d690-4917-jkhg-6c79b-1\"]}");
    		accessAccountsBean.setAccountMappingBean(new Gson().fromJson(userAccessDetails.getAccessDetails(), AccountMappingBean.class));
           Assert.assertEquals(accountBeansList.get(0).getIdentifier(), getAccountBL.process(accessAccountsBean).get(0).getIdentifier());
    }

}
