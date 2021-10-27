package com.heal.dashboard.service.dao.mysql;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.heal.dashboard.service.entities.TxnAndGroupBean;
import com.heal.dashboard.service.exception.ServerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class TransactionDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<TxnAndGroupBean> getTxnAndGroupPerService(int accountId) throws ServerException {
		try {
			String query = "select t.id as txnId,tm.tag_key as serviceId ,t.name as txnName,t.status,t.audit_enabled as isAutoEnabled,t.is_autoconfigured as isAutoConfigured,t.user_details_id  as userDetailsId,m.name as transactionTypeName,t.pattern_hashcode as patternHashCode,t.description,t.identifier, t.account_id  as accountId, t.is_business_txn as isBusinessTransaction, (select group_concat(txn_group_id) from transaction_group_mapping where object_id=t.id and object_ref_table='transaction' ) as tagListString from transaction t, tag_mapping tm, mst_sub_type m where t.status=1 and t.monitor_enabled = 1 and t.id = tm.object_id and tm.tag_id=1 and tm.object_ref_table ='transaction'and tm.account_id = t.account_id  and t.account_id = ? and m.id = t.transaction_type_id";
			return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(TxnAndGroupBean.class),accountId);
		} catch (DataAccessException e) {
			log.error("Invalid input parameter/s provided. Details:");
			throw new ServerException("Error in getTxnAndGroupPerService () method of TransactionDao  Invalid input parameter/s provided. Details: accountID =" + accountId);
		}
	}
	
}
