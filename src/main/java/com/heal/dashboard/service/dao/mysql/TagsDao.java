package com.heal.dashboard.service.dao.mysql;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.heal.dashboard.service.entities.TagDetails;
import com.heal.dashboard.service.entities.TagMapping;
import com.heal.dashboard.service.exception.ServerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class TagsDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	 public  List<TagMapping> getTagMappingDetails(int tagId,int objectId,String objectRefTable,int accountId) throws ServerException {
	  		try {
	  			String query = "select id, tag_id , object_id , object_ref_table , tag_key , tag_value from tag_mapping where tag_id = ? and object_id = ? and object_ref_table = ? and account_id = ?";
	  			return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(TagMapping.class),tagId,objectId,objectRefTable,accountId);
	  		} catch (DataAccessException e) {
	  			log.error("Error while fetching tag_mapping information", e);
	  			throw new ServerException("Error in getTagMappingDetails() method while fetching tag_mapping information for accountId : "+accountId);
	  		}
	  	}
	 
	 public  TagDetails getTagDetails(String name, int accountId) throws ServerException {
	  		try {
	  			String query = "select id,name,tag_type_id ,is_predefined ,ref_table ,created_time ,updated_time ,account_id ,user_details_id ,ref_where_column_name ,ref_select_column_name from tag_details where name = ? and account_id = ?";
	  			return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(TagDetails.class),name,accountId);
	  		} catch (DataAccessException e) {
	  			log.error("Error while fetching tag_details information", e);
	  			throw new ServerException("Error in getTagDetails() method  while fetching tag_details information for accountId : "+accountId);
	  		}
	  	}
	 
	 public  List<TagMapping> getTagMappingDetailsByTagKey(String tagKey, String objectRefTable,int accountId) throws ServerException {
	  		try {
	  			String query = "select id, tag_id , object_id , object_ref_table , tag_key , tag_value from tag_mapping where tag_key = ? and object_ref_table = ? and account_id = ?";
	  			return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(TagMapping.class),tagKey,objectRefTable,accountId);
	  		} catch (DataAccessException e) {
	  			log.error("Error in getTagMappingDetailsByTagKey method  while fetching tag_details information", e);
	  			throw new ServerException("Error in getTagMappingDetailsByTagKey() method while fetching tag_details information for accountId : "+accountId);
	  		}
	  	}
	
	 public  List<TagMapping> getTagMappingDetailsByAccountId(int accountId) throws ServerException {
	  		try {
	  			String query = "select id, tag_id , object_id , object_ref_table , tag_key , tag_value,created_time createdTime,updated_time updatedTime,account_id accountId,user_details_id userDetailsId  from tag_mapping where account_id = ?";
	  			return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(TagMapping.class),accountId);
	  		} catch (DataAccessException e) {
	  			log.error("Error while fetching tag_mapping information", e);
	  			throw new ServerException("Error in getTagMappingDetailsByAccountId() method while fetching tag_mapping information for accountId : "+accountId);
	  		}
	  	}

}
