package com.heal.dashboard.service.businesslogic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.heal.dashboard.service.businesslogic.account.ApplicationBL;
import com.heal.dashboard.service.dao.mysql.AccountDao;
import com.heal.dashboard.service.entities.AccountBean;
import com.heal.dashboard.service.entities.ApplicationResponseBean;
import com.heal.dashboard.service.entities.Controller;
import com.heal.dashboard.service.entities.TxnAndGroupBean;
import com.heal.dashboard.service.entities.UserAccessBean;
import com.heal.dashboard.service.entities.UserAccessDetails;
import com.heal.dashboard.service.entities.UtilityBean;
import com.heal.dashboard.service.exception.ClientException;
import com.heal.dashboard.service.exception.ServerException;
import com.heal.dashboard.service.pojo.RequestObject;
import com.heal.dashboard.service.util.Constants;

@RunWith(SpringRunner.class)
public class ApplicationBLTest {

	@InjectMocks
	ApplicationBL applicationBL;
	@Mock
	AccountDao acountDao;
	@Mock
	CommonServiceBL commonServiceBL;
	@Mock
	List<AccountBean> accountBeansList;
	@Mock
	UserAccessDetails userAccessDetails;

	private List<TxnAndGroupBean> txnAndGroupList;
	private UserAccessBean userAccessBean;
	private RequestObject<String> requestObject;
	private List<Controller> controllerBeanList;

	@Before
	public void setup() {
		// setting up mock data in accountBean
		accountBeansList = new ArrayList<>();
		AccountBean accountBean = new AccountBean();
		accountBean.setId(2);
		;
		accountBean.setIdentifier("7640123a-fbde-4fe5-9812-581cd1e3a9c1");
		accountBean.setName("India");
		accountBeansList.add(accountBean);

		// setting up mock data in userAccessDetails
		userAccessDetails = new UserAccessDetails();
		String[] identifier = { "7640123a-fbde-4fe5-9812-581cd1e3a9c1", "a-d681ef13-d690-4917-jkhg-6c79b-1" };
		List<String> identifierList = new ArrayList<>();
		identifierList.add("7640123a-fbde-4fe5-9812-581cd1e3a9c1");
		identifierList.add("a-d681ef13-d690-4917-jkhg-6c79b-2");
		userAccessDetails.setApplicationIdentifiers(identifierList);

		// setting up mock data in userAccessDetails
		userAccessBean = new UserAccessBean();
		userAccessBean.setAccessDetails("{\"accounts\": [\"*\"]}");
		userAccessBean.setId(1);
		userAccessBean.setUpdatedTime(LocalDateTime.now());
		userAccessBean.setCreatedTime(LocalDateTime.now());
		userAccessBean.setUserIdentifier("7640123a-fbde-4fe5-9812-581cd1e3a9c1");

		// setting up mock data in TxnAndGroupBean
		txnAndGroupList = new ArrayList<>();
		TxnAndGroupBean txnAndGroupBean = new TxnAndGroupBean();
		txnAndGroupBean.setIdentifier("qa-d681ef13-d690-4917-jkhg-6c79b-1");
		txnAndGroupBean.setDescription("test");
		txnAndGroupBean.setIsAutoConfigured(1);
		txnAndGroupBean.setServiceId("1");
		txnAndGroupList.add(txnAndGroupBean);

		// setting up mock data in Controller
		Controller controller = new Controller();
		controller.setAccountId(1);
		controller.setAppId("2");
		controller.setName("test");
		controller.setStatus(1);
		controller.setControllerTypeId(1);
		controller.setIdentifier("7640123a-fbde-4fe5-9812-581cd1e3a9c1");
		controllerBeanList = new ArrayList<Controller>();
		controllerBeanList.add(controller);
	}

	@Test
	public void getClientValidation_Success() throws Exception {
		requestObject = new RequestObject<String>();
		Map<String, String> params = new HashMap<String, String>();
		requestObject.setBody("7640123a-fbde-4fe5-9812-581cd1e3a9c1");
		requestObject.addHeaders(Constants.AUTHORIZATION_TOKEN, "7640123a-fbde-4fe5-9812-581cd1e3a9c1");

		Assert.assertEquals("7640123a-fbde-4fe5-9812-581cd1e3a9c1",
				applicationBL.clientValidation(requestObject).getAuthToken());
	}

	@Test(expected = ClientException.class)
	public void getClientValidation_ClientException1() throws ClientException {
		requestObject = new RequestObject<String>();
		requestObject.addHeaders(Constants.AUTHORIZATION_TOKEN, null);
		applicationBL.clientValidation(requestObject);

	}

	@Test
	public void serverValidation() throws Exception {
		UtilityBean<String> utilityBean = UtilityBean.<String>builder()
				.authToken("7640123a-fbde-4fe5-9812-581cd1e3a9c1")
				.accountIdentifier("7640123a-fbde-4fe5-9812-581cd1e3a9c1")
				.pojoObject("7640123a-fbde-4fe5-9812-581cd1e3a9c1").build();
		Mockito.when(acountDao.fetchUserAccessDetailsUsingIdentifier(Mockito.anyString())).thenReturn(userAccessBean);
		Mockito.when(acountDao.getAccountDetails(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(accountBeansList);
		Mockito.when(commonServiceBL.extractUserAccessDetails(Mockito.anyString(), Mockito.any()))
				.thenReturn(userAccessDetails);
		Mockito.when(commonServiceBL.getTransactionForApplication(Mockito.anyInt())).thenReturn(txnAndGroupList);
		Mockito.when(commonServiceBL.getControllersByType(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(controllerBeanList);

		Assert.assertEquals(2, applicationBL.serverValidation(utilityBean).getAccountId());
	}

	@Test(expected = ServerException.class)
	public void serverValidation_Case2() throws Exception {
		UtilityBean<String> utilityBean = UtilityBean.<String>builder()
				.pojoObject("7640123a-fbde-4fe5-9812-581cd1e3a9c1").build();
		Mockito.when(acountDao.fetchUserAccessDetailsUsingIdentifier(Mockito.anyString())).thenReturn(null);
		Mockito.when(acountDao.getAccountDetails(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(accountBeansList);
		Mockito.when(commonServiceBL.extractUserAccessDetails(Mockito.anyString(), Mockito.any()))
				.thenReturn(userAccessDetails);
		Mockito.when(commonServiceBL.getTransactionForApplication(Mockito.anyInt())).thenReturn(txnAndGroupList);
		Mockito.when(commonServiceBL.getControllersByType(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(controllerBeanList);
		Assert.assertEquals(2, applicationBL.serverValidation(utilityBean).getAccountId());
	}

	@Test
	public void processData() throws Exception {
		Assert.assertEquals(accountBeansList.get(0).getIdentifier(),
				applicationBL
						.process(new ApplicationResponseBean(1, controllerBeanList, txnAndGroupList, userAccessDetails))
						.get(0).getIdentifier());
	}

}
