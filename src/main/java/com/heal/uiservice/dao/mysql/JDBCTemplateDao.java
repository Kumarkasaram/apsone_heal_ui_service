package com.heal.uiservice.dao.mysql;

import com.heal.uiservice.entities.AccountBean;
import com.heal.uiservice.entities.UserAccessDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class JDBCTemplateDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserAccessDetails fetchUserAccessDetailsUsingIdentifier(String userIdentifier) {
        String query = "select a.access_details, a.user_identifier from user_access_details a where user_identifier=?";

        return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(UserAccessDetails.class), userIdentifier);
    }

    public List<AccountBean> getAccountDetails(String timezoneKey, String accountTableName) {
        String query = "select a.id, a.status, mt.timeoffset, mt.timeoffset, a.name, a.identifier, mt.time_zone_id, a.updated_time, " +
                "a.user_details_id, mt.abbreviation, mt.offset_name FROM account a join tag_mapping as tm on tm.object_id = a.id join tag_details as td on tm.tag_id = td.id " +
                "join mst_timezone as mt on mt.id = tm.tag_key where tm.object_ref_table = ? and td.name = ? and a.status = 1";

        return jdbcTemplate.query(query, new Object[]{accountTableName, timezoneKey}, BeanPropertyRowMapper.newInstance(AccountBean.class));
    }
}
