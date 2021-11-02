package com.heal.dashboard.service.businesslogic.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.heal.dashboard.service.businesslogic.BusinessLogic;
import com.heal.dashboard.service.dao.mysql.FeaturesDao;
import com.heal.dashboard.service.entities.DateComponentBean;
import com.heal.dashboard.service.entities.DateComponentDetailBean;
import com.heal.dashboard.service.entities.UtilityBean;
import com.heal.dashboard.service.exception.ClientException;
import com.heal.dashboard.service.exception.DataProcessingException;
import com.heal.dashboard.service.exception.ServerException;
import com.heal.dashboard.service.pojo.RequestObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DateComponentBL implements BusinessLogic<String, List<DateComponentBean>, DateComponentDetailBean> {

	@Autowired
	private FeaturesDao featuresDao;

	@Override
	public UtilityBean<String> clientValidation(RequestObject<String> requestObject) throws ClientException {
		return null;
	}

	@Override
	public List<DateComponentBean> serverValidation(UtilityBean<String> utilityBean) throws ServerException {
		List<DateComponentBean> dateComponentBeans = featuresDao.getDateTimeDropdownList();
		if (dateComponentBeans != null && !dateComponentBeans.isEmpty()) {
			return dateComponentBeans;
		} else {
			 log.error("there is no date component available");
			throw new ServerException("Error in getFeatures () method. details : there is no date component available ");
		}
	}

	@Override
	public DateComponentDetailBean process(List<DateComponentBean> bean) throws DataProcessingException {

		return new DateComponentDetailBean(bean);
	}
}
