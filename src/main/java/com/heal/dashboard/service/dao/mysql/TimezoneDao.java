package com.heal.dashboard.service.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.heal.dashboard.service.entities.TagDetails;
import com.heal.dashboard.service.entities.TagMapping;
import com.heal.dashboard.service.entities.TimezoneDetail;
import com.heal.dashboard.service.entities.UserAttributeBeen;
import com.heal.dashboard.service.entities.UserDetailsBean;
import com.heal.dashboard.service.exception.DataProcessingException;
import com.heal.dashboard.service.exception.ServerException;
import com.heal.dashboard.service.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class TimezoneDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public UserAttributeBeen getUserAttributes(String username) throws ServerException {
		try {
			String query = "select id id, user_identifier userIdentifier, contact_number contactNumber, email_address emailAddress, username username, "
					+ "user_details_id userDetailsId, created_time createdTime, updated_time updatedTime, status status, is_timezone_mychoice isTimezoneMychoice, "
					+ "mst_access_profile_id mstAccessProfileId, mst_role_id mstRoleId, is_notifications_timezone_mychoice isNotificationsTimezoneMychoice from user_attributes where username= ?";
			return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(UserAttributeBeen.class), username);
		} catch (DataAccessException e) {
			log.error("Invalid input parameter/s provided. Details:", username);
			throw new ServerException("Invalid input parameter/s provided. Details:" + username);
		}
	}

	public UserDetailsBean getUsers(String identifire) throws ServerException {
		try {
			String query = "SELECT u.id id, u.user_identifier userIdentifier, u.username userName, u.user_details_id createdBy, a.name userProfile,"
					+ " r.name role, u.status, u.created_time createdOn FROM user_attributes u, mst_roles r, mst_access_profiles a "
					+ " where u.mst_access_profile_id = a.id and u.mst_role_id = r.id and a.mst_role_id = r.id and u.user_identifier= ?";
			return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(UserDetailsBean.class),
					identifire);
		} catch (DataAccessException e) {
			log.error("Invalid input parameter/s provided. Details: user_identifier :", identifire);
			throw new ServerException("Invalid input parameter/s provided. Details: user_identifier:" + identifire);
		}
	}

	public TimezoneDetail getTimezonesById(String id) throws ServerException {
		try {
			String query = "select id, time_zone_id timeZoneId, timeoffset timeOffset, created_time createdTime, updated_time updatedTime, user_details_id userDetailsId, account_id accountId, offset_name offsetName from mst_timezone where id = ? and status = 1";
			return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(TimezoneDetail.class), id);
		} catch (DataAccessException e) {
			log.error("Timezone id does not exist in database :"+ id);
           throw new ServerException("Timezone id does not exist in database :"+ id);
		}
	}

	public int getUserTagMappingId(String objectRefTable, int objectId, int tagId) {
		try {
			String query = "select id from tag_mapping where object_ref_table = ? and object_id= ? and tag_id= ?";
			return jdbcTemplate.queryForObject(query, Integer.class, new Object[] { objectRefTable, objectId, tagId });
		} catch (DataAccessException e) {
			log.error("Invalid input parameter/s provided. Details:",
					"objectRefTable: " + objectRefTable + " objectRefTable:" + objectRefTable + " tagId:" + tagId);
			return 0;
		}
	}

	public TagDetails getTagDetails(String name, Integer accountId) throws DataProcessingException {
		try {
			String query = "select id,name,tag_type_id tagTypeId,is_predefined isPredefined,ref_table refTable,created_time createdTime,updated_time updatedTime,"
					+ "account_id accountId,user_details_id userDetailsId,ref_where_column_name refWhereColumnName,ref_select_column_name refSelectColumnName "
					+ "from tag_details where name = ? and account_id = ?";
			return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(TagDetails.class),
					new Object[] { name, accountId });
		} catch (DataAccessException e) {
			log.error("Tag does not exist in database. Name: "+name +"accountId"+ accountId);
            throw new DataProcessingException("Tag does not exist in database. Name: "+name +"accountId"+ accountId);
		}
	}

	public int updateUserTimezoneChoice(Integer isTimezoneMychoice, Integer isNotificationsTimezoneMychoice,
			String timeInGMT, String username, String updatingUserIdentifier) throws ServerException {
		try {
			String query = "UPDATE user_attributes SET is_timezone_mychoice=?, is_notifications_timezone_mychoice=?,"
					+ "updated_time= ?, user_details_id= ? where username= ?";
			return jdbcTemplate.update(query, new Object[] { isTimezoneMychoice, isNotificationsTimezoneMychoice,
					timeInGMT, updatingUserIdentifier, username });
		} catch (DataAccessException e) {
			log.error("failed to update data  in user_attributes table");
			throw new ServerException("failed to update data  in user_attributes table");
		}
	}

	public int deleteUserTagMapping(Integer id) throws ServerException {
		try {
			String query = "delete from tag_mapping where id=?";
			return jdbcTemplate.update(query, id);
		} catch (DataAccessException e) {
			log.error("failed to Delete  data from tag_mapping table");
			throw new ServerException("failed to Delete  data from tag_mapping table");
		}
	}

	public int updateUserTagMapping(Integer tagKey, String updatedTime, Integer id, String userDetailsId)
			throws ServerException {
		try {
			String query = "UPDATE tag_mapping set tag_key=?, updated_time=?, user_details_id=? where id=?";
			return jdbcTemplate.update(query, new Object[] { tagKey, updatedTime, userDetailsId, id });
		} catch (DataAccessException e) {	
			log.error("failed to update data  in tag_mapping table");
			throw new ServerException("failed to update data  in tag_mapping table");
		}
	}

	public int addUserTagMapping(TagMapping tagMappingDetails) throws ServerException {
		try {
			String query = "insert into tag_mapping (tag_id,object_id,object_ref_table,tag_key,tag_value,created_time,updated_time,account_id,user_details_id) values (?,?,?,?,?,?,?,?,?)";
			return jdbcTemplate.update(query, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					String timeInGMT = DateUtil.getTimeInGMT(System.currentTimeMillis());
					ps.setInt(1, tagMappingDetails.getTagId());
					ps.setInt(2, tagMappingDetails.getObjectId());
					ps.setString(3, tagMappingDetails.getObjectRefTable());
					ps.setString(4, tagMappingDetails.getTagKey());
					ps.setString(5, tagMappingDetails.getTagValue());
					ps.setString(6, timeInGMT);
					ps.setString(7, timeInGMT);
					ps.setInt(8, tagMappingDetails.getAccountId());
					ps.setString(9, tagMappingDetails.getUserDetailsId());
				}
			});
		} catch (DataAccessException e) {
			log.error("failed to add data  in tag_mapping table");
			throw new ServerException("failed to update data  in tag_mapping table");
		}

	}

}
