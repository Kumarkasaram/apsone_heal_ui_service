package com.heal.dashboard.service.businesslogic;

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

import com.heal.dashboard.service.dao.mysql.AccountDao;
import com.heal.dashboard.service.dao.mysql.ControllerDao;
import com.heal.dashboard.service.dao.mysql.MasterDataDao;
import com.heal.dashboard.service.dao.mysql.TagsDao;
import com.heal.dashboard.service.dao.mysql.TransactionDao;
import com.heal.dashboard.service.entities.AccountBean;
import com.heal.dashboard.service.entities.TagDetails;
import com.heal.dashboard.service.entities.TagMapping;
import com.heal.dashboard.service.entities.TxnAndGroupBean;
import com.heal.dashboard.service.entities.UserAccessBean;
import com.heal.dashboard.service.exception.ServerException;

@RunWith(SpringRunner.class)
public class CommonServiceBLTest {

	@InjectMocks
	CommonServiceBL commonServiceBL;
	@Mock
	private AccountDao accountDao;
	@Mock
	private ControllerDao controllerDao;
	@Mock
	private TagsDao tagsDao;
	@Mock
	private MasterDataDao MasterDataDao;
	@Mock
	private TransactionDao transactionDao;

	private UserAccessBean userAccessBean;
	private List<AccountBean> accountBeansList;

	@Before
	public void setup() {

		// setting up mock data in accountBean
		accountBeansList = new ArrayList<>();
		AccountBean accountBean = new AccountBean();
		accountBean.setAccountId(2);
		accountBean.setIdentifier("7640123a-fbde-4fe5-9812-581cd1e3a9c1");
		accountBean.setName("India");
		accountBean.setId(2);
		accountBean.setStatus(1);
		accountBean.setAbbreviation("test");
		accountBeansList.add(accountBean);

		// setting up mock data in userAccessBean
		userAccessBean = new UserAccessBean();
		userAccessBean.setAccessDetails("{\"accounts\": [\"*\"]}");
		userAccessBean.setId(1);
		userAccessBean.setUpdatedTime(LocalDateTime.now());
		userAccessBean.setCreatedTime(LocalDateTime.now());
		userAccessBean.setUserIdentifier("7640123a-fbde-4fe5-9812-581cd1e3a9c1");
		// = new UserAccessAccountsBean(new
		// Gson().fromJson(userAccessDetails.getAccessDetails(),
		// AccountMappingBean.class), accountBeansList)

	}

//	@Test
//	public void extractUserAccessDetails() throws ServerException {
//		Assert.assertEquals(
//				"UserAccessDetails(applicationIdentifiers=[], applicationIds=[], serviceIds=[], serviceIdentifiers=[], transactionIds=[])",
//				commonServiceBL.extractUserAccessDetails(userAccessBean.getAccessDetails(), accountBeansList.get(0))
//						.toString());
//
//	}

	@Test
	public void getTransactionForApplication() throws ServerException {
		List<TxnAndGroupBean> txnList = new ArrayList<>();
		TxnAndGroupBean txnAndGroupBean = new TxnAndGroupBean();
		txnAndGroupBean.setIdentifier("7640123a-fbde-4fe5-9812-581cd1e3a9c1");
		txnAndGroupBean.setDescription("test");
		txnAndGroupBean.setServiceId("2");
		txnList.add(txnAndGroupBean);
		Mockito.when(transactionDao.getTxnAndGroupPerService(Mockito.anyInt())).thenReturn(txnList);
		Assert.assertEquals("2", commonServiceBL.getTransactionForApplication(1).get(0).getServiceId());

	}

	@Test
	public void getServiceTags() throws ServerException {
		TagDetails layerTag = new TagDetails();
		layerTag.setAccountId(1);
		layerTag.setId(1);
		layerTag.setName("test");
		layerTag.setTagTypeId(2);
		layerTag.setUserDetailsId("7640123a-fbde-4fe5-9812-581cd1e3a9c1");
		List<TagMapping> txnList = new ArrayList<>();
		TagMapping tagMapping = new TagMapping();
		tagMapping.setAccountId(2);
		tagMapping.setId(2);
		tagMapping.setTagId(0);
		tagMapping.setTagKey("test");
		tagMapping.setTagValue("value");
		txnList.add(tagMapping);

		Mockito.when(tagsDao.getTagDetails(Mockito.anyString(), Mockito.anyInt())).thenReturn(layerTag);
		Mockito.when(
				tagsDao.getTagMappingDetails(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(txnList);

		Assert.assertEquals(2, commonServiceBL.getServiceTags(1, 5).get(0).getAccountId());

	}

}
