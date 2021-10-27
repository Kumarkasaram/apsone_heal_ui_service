package com.heal.dashboard.service.businesslogic.timezone;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.heal.dashboard.service.businesslogic.BusinessLogic;
import com.heal.dashboard.service.dao.mysql.TimezoneDao;
import com.heal.dashboard.service.entities.TagDetails;
import com.heal.dashboard.service.entities.TagMapping;
import com.heal.dashboard.service.entities.TimezoneDetail;
import com.heal.dashboard.service.entities.UserAttributeBeen;
import com.heal.dashboard.service.entities.UserDetailsBean;
import com.heal.dashboard.service.entities.UserTimezoneRequestData;
import com.heal.dashboard.service.entities.UtilityBean;
import com.heal.dashboard.service.exception.ClientException;
import com.heal.dashboard.service.exception.DataProcessingException;
import com.heal.dashboard.service.exception.ServerException;
import com.heal.dashboard.service.pojo.RequestObject;
import com.heal.dashboard.service.util.Constants;
import com.heal.dashboard.service.util.DateUtil;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class UserTimezoneBL implements BusinessLogic<UserTimezoneRequestData, UserTimezoneRequestData, String> {

	@Autowired
	TimezoneDao timezoneDao;
	
	@Override
	public UtilityBean<UserTimezoneRequestData> clientValidation(RequestObject<UserTimezoneRequestData> requestObject)
			throws ClientException {
		String JwtToken = requestObject.getHeaders().get(Constants.AUTHORIZATION_TOKEN.toLowerCase());
		 if (null == JwtToken || JwtToken.trim().isEmpty()) {
	            throw new ClientException(Constants.AUTHORIZATION_TOKEN_IS_NULL_OR_EMPTY);
	        }
	       // String userId = Utility.extractUserIdFromJWT(jwtToken);
	        String authToken = JwtToken;  // TODO replace with above line
	        if(null == authToken || authToken.trim().isEmpty()) {
	            throw new ClientException("User details extraction failure");
	        }
		 UserTimezoneRequestData requestData = requestObject.getBody();
	        return UtilityBean.<UserTimezoneRequestData>builder()
	                .authToken(authToken)
	                .pojoObject(requestData).build();
	}
	
       

	@Override
	public UserTimezoneRequestData serverValidation(UtilityBean<UserTimezoneRequestData> utilityBean) throws ServerException {
		UserTimezoneRequestData userTimezoneRequestData = new UserTimezoneRequestData();
		String userName = utilityBean.getPojoObject().getUsername();
		UserAttributeBeen userAttributeBeen = timezoneDao.getUserAttributes(userName);
		if (userAttributeBeen.getStatus() == 1) {
            userTimezoneRequestData.setUserTimezonePojo(utilityBean.getPojoObject().getUserTimezonePojo());
            userTimezoneRequestData.setUserAttributeBeen(userAttributeBeen);
        } else {
            log.error("User account is in-active for My Profile changes. Details:", utilityBean.getPojoObject().getUsername());
            throw new ServerException("User account is in-active for My Profile changes"+ utilityBean.getPojoObject().getUsername());
        }
		UserDetailsBean userDetailsBean = timezoneDao.getUsers(utilityBean.getAuthToken());
		int timeZoneId = utilityBean.getPojoObject().getUserTimezonePojo().getTimezoneId();
		 if (timeZoneId > 0) {
	            TimezoneDetail timezoneDetail = timezoneDao.getTimezonesById(String.valueOf(timeZoneId));
	            userTimezoneRequestData.setTimezoneDetail(timezoneDetail);
	        }
	        userTimezoneRequestData.setUserDetailsBean(userDetailsBean);
	        return userTimezoneRequestData;
	}

	@Override
	@Transactional(isolation= Isolation.READ_COMMITTED,propagation =Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
	public String process(UserTimezoneRequestData requestBean) throws DataProcessingException {
		try {
		 TagDetails tagDetailsBean =timezoneDao.getTagDetails(Constants.TIME_ZONE_TAG,Constants.DEFAULT_ACCOUNT_ID);
	        int tagMappingId = timezoneDao.getUserTagMappingId(Constants.USER_ATTRIBUTES_TABLE_NAME_MYSQL,
	        		requestBean.getUserAttributeBeen().getId(), tagDetailsBean.getId());
	        String userIdentifier = batchUpdate(requestBean,tagMappingId,tagDetailsBean);
			return userIdentifier;
		} catch (DataAccessException ex) {
			log.debug("excption"+ex.getStackTrace());
		}
		return null;
	}
	
	
	
	public String batchUpdate (UserTimezoneRequestData requestData,int tagMappingId,TagDetails tagDetailsDean) throws DataProcessingException {
		try {
			String timeInGMT = DateUtil.getTimeInGMT(System.currentTimeMillis());
			int res = timezoneDao.updateUserTimezoneChoice(requestData.getUserTimezonePojo().getIsTimezoneMychoice(),
					requestData.getUserTimezonePojo().getIsNotificationsTimezoneMychoice(), timeInGMT,
					requestData.getUserAttributeBeen().getUsername(),
					requestData.getUserDetailsBean().getUserIdentifier());
			
			if (tagMappingId != 0 && requestData.getUserTimezonePojo().getTimezoneId() == 0) {
				res = timezoneDao.deleteUserTagMapping(tagMappingId); // Delete from tag_mapping
				
			} else if (tagMappingId != 0 && requestData.getUserTimezonePojo().getTimezoneId() != 0) {
				res = timezoneDao.updateUserTagMapping(requestData.getTimezoneDetail().getId(), timeInGMT, tagMappingId,
						requestData.getUserDetailsBean().getCreatedBy()); // Update tag_mapping
				
			} else if (tagMappingId == 0 && requestData.getUserTimezonePojo().getTimezoneId() != 0) {
				TagMapping tagMappingDetails = populateTagMapping(requestData, tagDetailsDean,
						requestData.getTimezoneDetail());
				res = timezoneDao.addUserTagMapping(tagMappingDetails); // Insert into tag_mapping
			}
			if (res > 0) {
				return requestData.getUserDetailsBean().getUserIdentifier();
			} else {
				throw new DataProcessingException(
						"Failed to update timezone for user id: {0} : " + requestData.getUserDetailsBean().getId());
			}
		} catch (Exception ex) {
			throw new DataProcessingException(ex.getCause(), "Failed to update user timezone");
		}

	}
	
	 public TagMapping populateTagMapping(UserTimezoneRequestData configData, TagDetails tagDetailsBean, TimezoneDetail timezoneDetail) {
	        TagMapping tagMappingDetails = new TagMapping();
	        tagMappingDetails.setAccountId(Constants.DEFAULT_ACCOUNT_ID);
	        tagMappingDetails.setObjectId(configData.getUserAttributeBeen().getId());
	        tagMappingDetails.setObjectRefTable(Constants.USER_ATTRIBUTES_TABLE_NAME_MYSQL);
	        tagMappingDetails.setTagValue(String.valueOf(timezoneDetail.getTimeOffset()));
	        tagMappingDetails.setTagId(tagDetailsBean.getId());
	        tagMappingDetails.setTagKey(String.valueOf(configData.getUserTimezonePojo().getTimezoneId()));
	        tagMappingDetails.setUserDetailsId(configData.getUserDetailsBean().getCreatedBy());
	        return tagMappingDetails;
	    }

	
}
