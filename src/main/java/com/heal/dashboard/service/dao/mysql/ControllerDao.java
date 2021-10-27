package com.heal.dashboard.service.dao.mysql;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.heal.dashboard.service.entities.Controller;
import com.heal.dashboard.service.entities.ControllerBean;
import com.heal.dashboard.service.entities.ViewApplicationServiceMappingBean;
import com.heal.dashboard.service.exception.ServerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ControllerDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	    
	    public  List<Controller> getControllerList(int accountId) throws ServerException {
	  		try {
	  			String query = "select id as appId ,name,controller_type_id ,identifier, monitor_enabled  as monitoringEnabled,account_id , status from controller where account_id = ?";
	  			return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Controller.class),accountId);
	  		} catch (DataAccessException e) {
	  			log.error("Error while fetching controller information", e);
	  			throw new ServerException("Error in ControllerDao.getControllerList while fetching controller information for accountId : "+accountId);
	  		}
	  	}
	    
	    public  List<ViewApplicationServiceMappingBean> getApplicationServicesByAccount(int accountId) throws ServerException {
	  		try {
	  			String query = "select service_id , service_identifier , application_id , application_identifier ,application_name  from view_application_service_mapping where account_id = ?";
	  			return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(ViewApplicationServiceMappingBean.class),accountId);
	  		} catch (DataAccessException e) {
	  			log.error("Error while fetching controller information", e);
	  			throw new ServerException("Error while fetching view_application_service_mapping information for accountId : "+accountId);
	  		}
	  	}
	    
	    
	    public  List<ControllerBean> getAllServicesForAccount(int accountId) throws ServerException {
	  		try {
	  			String query = "select service_id as id , service_name  as name, service_identifier as identifier ,account_id as accountId ,1  as status, 192  as controllerTypeId from view_application_service_mapping where account_id = ?";
	  			return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(ControllerBean.class),accountId);
	  		} catch (DataAccessException e) {
	  			log.error("Error while fetching controller information", e);
	  			throw new ServerException("Error in ControllerDao.getAllServicesForAccount()  while fetching view_application_service_mapping information for accountId : "+accountId);
	  		}
	  	}
	    
	    public  List<ControllerBean> getServicesByAppId(int applicationId,int accountId) throws ServerException {
	  		try {
	  			String query = "select service_id as id , service_name  as name, service_identifier as identifier ,account_id as accountId ,1  as status, 192  as controllerTypeId from view_application_service_mapping where application_id = ?, account_id = ?";
	  			return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(ControllerBean.class),applicationId,accountId);
	  		} catch (DataAccessException e) {
	  			log.error("Error while fetching controller information", e);
	  			throw new ServerException("Error while fetching view_application_service_mapping information for accountId : "+accountId);
	  		}
	  	}
	    
	    public  List<ControllerBean> getApplicationsBySvcId(int serviceId,int accountId) throws ServerException {
	  		try {
	  			String query = "select application_id as id , application_name as name, application_identifier as identifier , 1 as status ,account_id as accountId, 191 as controllerTypeId from view_application_service_mapping where service_id = ? and account_id = ?";
	  			return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(ControllerBean.class),serviceId,accountId);
	  		} catch (DataAccessException e) {
	  			log.error("Error while fetching controller information", e);
	  			throw new ServerException("Error while fetching view_application_service_mapping information for accountId : "+accountId);
	  		}
	  	}
	    
	  

}

