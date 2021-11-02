package com.heal.dashboard.service.test_service;

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
import com.heal.dashboard.service.businesslogic.account.ApplicationBL;
import com.heal.dashboard.service.businesslogic.account.GetAccountsBL;
import com.heal.dashboard.service.entities.AccountBean;
import com.heal.dashboard.service.entities.AccountMappingBean;
import com.heal.dashboard.service.entities.ApplicationDetailBean;
import com.heal.dashboard.service.entities.ApplicationResponseBean;
import com.heal.dashboard.service.entities.UserAccessAccountsBean;
import com.heal.dashboard.service.entities.UserAccessBean;
import com.heal.dashboard.service.entities.UtilityBean;
import com.heal.dashboard.service.exception.ClientException;
import com.heal.dashboard.service.pojo.RequestObject;
import com.heal.dashboard.service.service.GetAccountsService;
import com.heal.dashboard.service.util.Constants;

@RunWith(SpringRunner.class)
public class GetAccountsServiceTest {

	

	@InjectMocks
    GetAccountsService  getAccountsService;
	@Mock
    GetAccountsBL  getAccountBL;
    @Mock
    List<AccountBean> accountBeansList;
    @Mock
    UserAccessBean userAccessDetails;
    @Mock
	ApplicationBL applicationBL;
    
    UserAccessAccountsBean accessAccountsBean;
    RequestObject requestObject;
    
    List<ApplicationDetailBean> applicationDetailBeanList;
    ApplicationResponseBean applicationResponseBean;
    
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
        userAccessDetails = new UserAccessBean();
        userAccessDetails.setAccessDetails("{\"accounts\": [\"*\"]}");
        userAccessDetails.setId(1);
        userAccessDetails.setUpdatedTime(LocalDateTime.now());
        userAccessDetails.setCreatedTime(LocalDateTime.now());
        userAccessDetails.setUserIdentifier("7640123a-fbde-4fe5-9812-581cd1e3a9c1");
        accessAccountsBean = new UserAccessAccountsBean(new Gson().fromJson(userAccessDetails.getAccessDetails(), AccountMappingBean.class), accountBeansList);
    	
        requestObject = new RequestObject();
        requestObject.addHeaders(Constants.AUTHORIZATION_TOKEN, "7640123a-fbde-4fe5-9812-581cd1e3a9c1");
    	
    }	

	    @Test
	    public void getAccountList_Success() throws Exception {
	        Mockito.when(getAccountBL.clientValidation(Mockito.any())).thenReturn(UtilityBean.<String>builder().pojoObject("7640123a-fbde-4fe5-9812-581cd1e3a9c1").build());
	        Mockito.when(getAccountBL.serverValidation(Mockito.any())).thenReturn(accessAccountsBean);
	        Mockito.when(getAccountBL.process(Mockito.any())).thenReturn(accountBeansList);
	        Assert.assertEquals("India", getAccountsService.getAccountList("7640123a-fbde-4fe5-9812-581cd1e3a9c1").get(0).getName());
	    }

	    @Test(expected =Exception.class)
	    public void getAccountList_BadRequestException() throws Exception {
	           Mockito.when(getAccountBL.clientValidation(Mockito.any())).thenThrow(new ClientException("authentication failed"));
	        Assert.assertNull( getAccountsService.getAccountList(null));
	    }
	    @Test(expected =RuntimeException.class)
	    public void getAccountList_InternalServerError() throws Exception {
	        Mockito.when(getAccountBL.clientValidation(Mockito.any())).thenReturn(UtilityBean.<String>builder().pojoObject("7640123a-fbde-4fe5-9812-581cd1e3a9c1").build());
	        Mockito.when(getAccountBL.serverValidation(Mockito.any())).thenReturn(accessAccountsBean);
	        Mockito.when(getAccountBL.process(Mockito.any())).thenThrow(new RuntimeException());
	        Assert.assertNull( getAccountsService.getAccountList("7640123a-fbde-4fe5-9812-581cd1e3a9c1"));
	    }
	    
	    @Test
	    public void getApplicationList_Success() throws Exception {
	    	 applicationDetailBeanList = new ArrayList<>();
	         ApplicationDetailBean applicationDetailBean = new ApplicationDetailBean();
	         applicationDetailBean.setId(1);
	         applicationDetailBean.setIdentifier("7640123a-fbde-4fe5-9812-581cd1e3a9c1");
	         applicationDetailBean.setName("test");
	         applicationDetailBeanList.add(applicationDetailBean);       
	        Mockito.when(applicationBL.clientValidation(Mockito.any())).thenReturn(UtilityBean.<String>builder().pojoObject("7640123a-fbde-4fe5-9812-581cd1e3a9c1").build());
	        Mockito.when(applicationBL.serverValidation(Mockito.any())).thenReturn(new ApplicationResponseBean());
	        Mockito.when(applicationBL.process(Mockito.any())).thenReturn(applicationDetailBeanList);
	        Assert.assertEquals("test", getAccountsService.getApplicationList("7640123a-fbde-4fe5-9812-581cd1e3a9c1","").get(0).getName());
	    }
	
}
