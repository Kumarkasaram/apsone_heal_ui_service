package com.heal.dashboard.service.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.heal.dashboard.service.businesslogic.account.GetAccountsBL;
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

import com.heal.dashboard.service.entities.AccountBean;
import com.heal.dashboard.service.entities.ApplicationDetailBean;
import com.heal.dashboard.service.entities.topology.Edges;
import com.heal.dashboard.service.entities.topology.Nodes;
import com.heal.dashboard.service.entities.topology.TopologyDetails;
import com.heal.dashboard.service.service.GetAccountsService;
import com.heal.dashboard.service.service.TopologyDetailsService;
@RunWith(SpringRunner.class)
public class AccountControllerTest {

    @InjectMocks
    AccountController accountcontroller;
    @Mock
    GetAccountsService getAccountService;
    @MockBean
    GetAccountsBL getAccountsBL;
    @Mock
    List<AccountBean> accountBeansList;
    @Mock
    TopologyDetailsService topologyDetailsService;
    
    TopologyDetails topologyDetails;
    
    List<ApplicationDetailBean> applicationDetailBeanList;
   

    @Before
    public void setup() {
    	 accountBeansList = new ArrayList<>();
         AccountBean  accountBean = new AccountBean();
         accountBean.setAccountId(2);
         accountBean.setIdentifier("qa-d681ef13-d690-4917-jkhg-6c79b-1");
         accountBean.setName("India");
         accountBeansList.add(accountBean);
         
         
          topologyDetails = new TopologyDetails();
         List<Nodes> nodeslist = new ArrayList<Nodes>();
         List<Edges> edgeslist = new ArrayList<>();
         
         Nodes nodes = new Nodes();
         nodes.setId("1");
         nodes.setIdentifier("qa-d681ef13-d690-4917-jkhg-6c79b-1");
         nodes.setName("test");
         nodes.setStartNode(true);
         nodeslist.add(nodes);
         
         Edges edges = new Edges();
         edges.setSource("test");
         edges.setTarget("qa-d681ef13-d690-4917-jkhg-6c79b-1");
         edges.setData(new HashMap<String, String>());
         edgeslist.add(edges);
         
         applicationDetailBeanList = new ArrayList<>();
         ApplicationDetailBean applicationDetailBean = new ApplicationDetailBean();
         applicationDetailBean.setId(1);
         applicationDetailBean.setIdentifier("qa-d681ef13-d690-4917-jkhg-6c79b-1");
         applicationDetailBean.setName("test");
         applicationDetailBeanList.add(applicationDetailBean);
       
         
  
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
    @Test
   	public void getApplicationList_OK() throws Exception {
   		Mockito.when(getAccountService.getApplicationList(Mockito.anyString(),Mockito.anyString())).thenReturn(applicationDetailBeanList);
   		Assert.assertEquals(HttpStatus.OK, accountcontroller.getApplicationList("testing","identifier").getStatusCode());
   	}
       
    @Test
	public void getServiceDetails() throws Exception {
		Mockito.when(topologyDetailsService.getServiceDetails(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(topologyDetails);
		Assert.assertEquals(HttpStatus.OK, accountcontroller.getServiceDetails("testing","","","").getStatusCode());
	}
    
    @Test
  	public void getTopologyDetails() throws Exception {
  		Mockito.when(topologyDetailsService.getTopologyDetails(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(topologyDetails);
  		Assert.assertEquals(HttpStatus.OK, accountcontroller.getTopologyDetails("testing","","").getStatusCode());
  	}

}
