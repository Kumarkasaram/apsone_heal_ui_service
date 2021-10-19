package com.heal.uiservice.dao.mysql;

import com.heal.uiservice.entities.AccountBean;
import com.heal.uiservice.entities.UserAccessDetails;
import com.heal.uiservice.entities.UserAccessDetailsMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Repository
public class JDBCTemplateDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    public UserAccessDetails fetchUserAccessDetailsUsingIdentifier (String identifier) {
        String sqlQuery_userAccess = "select * from  user_access_details where user_identifier  = ?";
        UserAccessDetails userAccessDetails = jdbcTemplate.queryForObject(
                sqlQuery_userAccess, new UserAccessDetailsMapper(), identifier);
        return userAccessDetails;
    }

    public List<AccountBean> getAccountDetails( String timezone,String  account) {
        String sqlQuery_accountBean = "SELECT a.id , a.name , mt.timeoffset ,a.identifier ,mt.time_zone_id ,"
        		+ " a.updated_time , a.user_details_id , mt.timeoffset , mt.abbreviation , offset_name , "
        		+ "a.user_details_id FROM account a, tag_mapping tm, mst_timezone mt, tag_details td where tm.object_id = a.id and tm.object_ref_table = ?  "
        		+ "and tm.tag_id = td.id and td.name = ?  and mt.id = tm.tag_key and a.status = 1";
        List<AccountBean> accountList = jdbcTemplate.query(sqlQuery_accountBean , (RowMapper<AccountBean>) (rs, rowNum) ->  new AccountBean(
                rs.getInt("id"),
                0,
                rs.getInt("timeoffset"),
                rs.getInt("timeoffset"),
                rs.getString("name"),
                rs.getString("identifier"),
                rs.getString("time_zone_id"),
                LocalDateTime.parse(rs.getString("updated_time"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                rs.getString("user_details_id"),
                rs.getString("offset_name"),
                rs.getString("abbreviation"),
                rs.getString("user_details_id")),new Object[] {account,timezone});
        return accountList;
    }
}
