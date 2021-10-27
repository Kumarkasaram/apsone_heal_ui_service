package com.heal.dashboard.service.businesslogic.topology;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.heal.dashboard.service.entities.topology.Edges;
import com.heal.dashboard.service.entities.topology.Nodes;
import com.heal.dashboard.service.entities.topology.TopologyDetails;
import com.heal.dashboard.service.entities.topology.TopologyValidationResponseBean;
import com.heal.dashboard.service.businesslogic.BusinessLogic;
import com.heal.dashboard.service.businesslogic.CommonServiceBL;
import com.heal.dashboard.service.businesslogic.TopologyUtilityBL;
import com.heal.dashboard.service.dao.mysql.AccountDao;
import com.heal.dashboard.service.entities.AccountBean;
import com.heal.dashboard.service.entities.UserAccessBean;
import com.heal.dashboard.service.entities.UserAccessDetails;
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
public class TopologyServiceBL implements BusinessLogic<String, TopologyValidationResponseBean, TopologyDetails> {

	@Autowired
	AccountDao accountDao;
	@Autowired
	CommonServiceBL commonServiceBL;
	@Autowired
	TopologyUtilityBL topologyUtility;

	@Override
	public UtilityBean<String> clientValidation(RequestObject<String> requestObject) throws ClientException {
		String jwtToken = requestObject.getHeaders().get(Constants.AUTHORIZATION_TOKEN);
		if (null == jwtToken || jwtToken.trim().isEmpty()) {
			throw new ClientException(Constants.AUTHORIZATION_TOKEN_IS_NULL_OR_EMPTY);
		}
		String identifier = requestObject.getParams().get("identifier");
		if (null == identifier || identifier.trim().isEmpty()) {
			throw new ClientException("identifier cant be null or empty");
		}

		// String userId = Utility.extractUserIdFromJWT(jwtToken);
		String userId = jwtToken;
		if (null == userId || userId.trim().isEmpty()) {
			throw new ClientException("User details extraction failure");
		}

		return UtilityBean.<String>builder().authToken(userId).accountIdentifier(identifier)
				.params(requestObject.getQueryParams()).build();
	}

	@Override
	public TopologyValidationResponseBean serverValidation(UtilityBean<String> utilityBean)
			throws ServerException {
		AccountBean accountBean = null;
		UserAccessDetails userAccessDetails = null;
		Timestamp date = null;
		List<Nodes> nodeList =null;
		List<Edges> edgesList =null;
		String applicationIdString = utilityBean.getParams().get("applicationId");
		List<AccountBean> accountsList = accountDao.getAccountDetails(Constants.TIME_ZONE_TAG,
				Constants.ACCOUNT_TABLE_NAME_MYSQL_DEFAULT);
		if (accountsList != null || !accountsList.isEmpty()) {
			Optional<AccountBean> accountOptional = accountsList.stream()
					.filter(it -> it.getIdentifier().equals(utilityBean.getAccountIdentifier())).findAny();
			if (accountOptional.isPresent()) {
				accountBean = accountOptional.get();
			} else {
				log.error("Error while fetching account detail information for  Details: ",
						utilityBean.getAccountIdentifier());
				throw new ServerException(
						"Exception occur in TopologyValidationResponseBean () method , Invalid input parameter/s provided. Details:, UserIdentifier :"
								+ utilityBean.getAccountIdentifier());
			}
		}

		UserAccessBean userAccessBean = accountDao.fetchUserAccessDetailsUsingIdentifier(utilityBean.getAuthToken());
		if (userAccessBean != null) {
			userAccessDetails = commonServiceBL.extractUserAccessDetails(userAccessBean.getAccessDetails(),
					accountBean);
			if (userAccessDetails == null) {
				log.error("Exception occur while extractUserAccessDetails");
				throw new ServerException(
						"Exception occur in TopologyValidationResponseBean () method while extractUserAccessDetails");
			}
		}
		
		try {
			date = new Timestamp(DateUtil.getDateInGMT(System.currentTimeMillis()).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  if (applicationIdString == null) {
			  nodeList = topologyUtility.getNodeList(accountBean, userAccessDetails, 0, date.getTime());
              edgesList = topologyUtility.getEdgeList(accountBean.getId(), 0);
          } else {
              int applicationId = Integer.parseInt(applicationIdString);
              nodeList = topologyUtility.getNodeList(accountBean, userAccessDetails, 0, date.getTime());
              edgesList = topologyUtility.getEdgeList(accountBean.getId(), 0);
          }
		return new TopologyValidationResponseBean(nodeList, edgesList, utilityBean.getParams().get("serviceId"));
	}

	@Override
	public TopologyDetails process(TopologyValidationResponseBean topologyValidationResponseBean)
			throws DataProcessingException {
		TopologyDetails topologyDetails = new TopologyDetails();
		topologyDetails.setNodes(topologyValidationResponseBean.getNodeList());
		topologyDetails.setEdges(topologyValidationResponseBean.getEdgesList());
		return topologyDetails;
	}

}
