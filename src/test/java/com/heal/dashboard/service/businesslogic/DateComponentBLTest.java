package com.heal.dashboard.service.businesslogic;

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

import com.heal.dashboard.service.businesslogic.account.DateComponentBL;
import com.heal.dashboard.service.dao.mysql.FeaturesDao;
import com.heal.dashboard.service.entities.DateComponentBean;
import com.heal.dashboard.service.entities.UtilityBean;
import com.heal.dashboard.service.pojo.RequestObject;
import com.heal.dashboard.service.util.Constants;

@RunWith(SpringRunner.class)
public class DateComponentBLTest {
    @InjectMocks
    private DateComponentBL dateComponentBL;

    @Mock
    FeaturesDao jdbcTemplateDao;
    List<DateComponentBean> dateComponentBeans;
    RequestObject<String> requestObject;

    @Before
    public void setup() {
        dateComponentBeans= new ArrayList<>();
        DateComponentBean dateComponentBean = new DateComponentBean();
        dateComponentBean.setLabel("Last 24 hours");
        dateComponentBean.setValue(24);
        dateComponentBean.setType("hours");
        dateComponentBeans.add(dateComponentBean);

    }

    @Test
    public void getClientValidation_Success() throws Exception {
        requestObject = new RequestObject<String>();
        requestObject.addHeaders(Constants.AUTHORIZATION_TOKEN, "7640123a-fbde-4fe5-9812-581cd1e3a9c1");
        Assert.assertEquals(null, dateComponentBL.clientValidation(requestObject));
    }

    @Test
    public void serverValidation() throws Exception {
        UtilityBean<String> utilityBean = UtilityBean.<String>builder().pojoObject("7640123a-fbde-4fe5-9812-581cd1e3a9c1").build();
        Mockito.when(jdbcTemplateDao.getDateTimeDropdownList()).thenReturn(dateComponentBeans);
        Assert.assertEquals(dateComponentBeans.get(0).getLabel(), dateComponentBL.serverValidation(utilityBean).get(0).getLabel());
    }
    @Test(expected = Exception.class)
    public void serverValidationCase2() throws Exception {
        UtilityBean<String> utilityBean = UtilityBean.<String>builder().pojoObject("7640123a-fbde-4fe5-9812-581cd1e3a9c1").build();
        dateComponentBeans.clear();
        Mockito.when(jdbcTemplateDao.getDateTimeDropdownList()).thenReturn(dateComponentBeans);
        Assert.assertEquals(null, dateComponentBL.serverValidation(utilityBean));
    }
    @Test
    public void processData() throws Exception {
        Assert.assertEquals(dateComponentBeans.get(0).getLabel(), dateComponentBL.process(dateComponentBeans).getDateComponentBeans().get(0).getLabel());
    }
}
