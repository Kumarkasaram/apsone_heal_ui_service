package com.heal.dashboard.service.dao.mysql;



import com.heal.dashboard.service.entities.AccountBean;
import com.heal.dashboard.service.entities.UserAccessDetails;
import com.heal.dashboard.service.exception.ServerException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class AccountDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

	public UserAccessDetails fetchUserAccessDetailsUsingIdentifier(String userIdentifier) throws ServerException {
		try {
			String query = "select a.access_details, a.user_identifier from user_access_details a where user_identifier=?";
			return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(UserAccessDetails.class),
					userIdentifier);
		} catch (DataAccessException e) {
			log.error("Error while fetching user access information for user [{}]. Details: ", userIdentifier, e);
			throw new ServerException("\"Invalid input parameter/s provided. Details:, UserIdentifier :"+userIdentifier);
		}
	}

    public List<AccountBean> getAccountDetails(String timezoneKey, String accountTableName) {
        String query = "select a.id, a.status, mt.timeoffset, mt.timeoffset, a.name, a.identifier, mt.time_zone_id, a.updated_time, " +
                "a.user_details_id, mt.abbreviation, mt.offset_name FROM account a join tag_mapping as tm on tm.object_id = a.id join tag_details as td on tm.tag_id = td.id " +
                "join mst_timezone as mt on mt.id = tm.tag_key where tm.object_ref_table = ? and td.name = ? and a.status = 1";
        return jdbcTemplate.query(query, new Object[]{accountTableName, timezoneKey}, BeanPropertyRowMapper.newInstance(AccountBean.class));
   
    }
}

