package com.heal.dashboard.service.dao.mysql;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.heal.dashboard.service.entities.AgentBean;
import com.heal.dashboard.service.exception.ServerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class AgentDao {
	 
	 	@Autowired
		JdbcTemplate jdbcTemplate;
		    
		    public  List<AgentBean> getAgentList() throws ServerException {
		  		try {
		  			String query = "select id,unique_token ,name,agent_type_id ,created_time ,updated_time ,user_details_id ,status,host_address ,mode,description from agent";
		  			return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(AgentBean.class));
		  		} catch (DataAccessException e) {
		  			log.error("Error while fetching  Detail getAgentList()", e);
		  			throw new ServerException("Error while fetching  Detail getAgentList()");
		  		}
		  	}
}
