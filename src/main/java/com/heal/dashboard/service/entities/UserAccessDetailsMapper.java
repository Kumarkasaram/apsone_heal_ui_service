package com.heal.dashboard.service.entities;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class UserAccessDetailsMapper implements RowMapper<UserAccessDetails> {

	public UserAccessDetails mapRow(ResultSet rs, int rows) throws SQLException {
		UserAccessDetails userAccessDetails = new UserAccessDetails();
		userAccessDetails.setId(rs.getInt("id"));
		userAccessDetails.setUserIdentifier(rs.getString("user_identifier"));
		userAccessDetails.setAccessDetails(rs.getString("access_details"));
		//userAccessDetails.setUserDetailsId(rs.getString("user_details_id"));
		return userAccessDetails;
	}

}
