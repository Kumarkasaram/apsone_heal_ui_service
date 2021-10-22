package com.heal.uiservice.controller;



import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.heal.uiservice.businesslogic.GetAccountsBL;
import com.heal.uiservice.controller.AccountController;
import com.heal.uiservice.entities.AccountBean;
import com.heal.uiservice.service.GetAccountsService;

@RunWith(SpringRunner.class)
public class AccountControllerTest {

    @InjectMocks
    AccountController accountcontroller;
    @Mock
    GetAccountsService getAccountService;
    @MockBean
    GetAccountsBL  getAccountsBL;
    @Mock
    List<AccountBean> accountBeansList;
   

    @Before
    public void setup() {
    	 accountBeansList = new ArrayList<>();
         AccountBean  accountBean = new AccountBean();
         accountBean.setAccountId(2);
         accountBean.setIdentifier("qa-d681ef13-d690-4917-jkhg-6c79b-1");
         accountBean.setName("India");
         accountBeansList.add(accountBean);
  
    }
	@Test(expected = RuntimeException.class)
	public void getAccountListTest_InternalServerErrror() {
		Mockito.when(getAccountService.getAccountList(Mockito.anyString())).thenThrow( new RuntimeException());
		 accountcontroller.getAccountList("tetsing");
	}
    
    @Test
	public void getAccountListTest_Ok() throws Exception {
		Mockito.when(getAccountService.getAccountList(Mockito.anyString())).thenReturn(accountBeansList);
		Assert.assertEquals(HttpStatus.OK, accountcontroller.getAccountList("testing").getStatusCode());
	}
}
