package com.heal.dashboard.service.dao.mysql;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.annotations.Query;
import com.heal.dashboard.service.exception.ServerException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Repository
public class AccountCassandraDao {

	 @Autowired
	 
	  CassandraOperations cassandraTemplate;
    
	 
	    public  Set<String> getSignalId(String accountIdentifier, Long fromTime, Long toTime) throws ServerException {
	    	List<Row> rows  = null;
	    	Set<String> signalIds = new HashSet<>();
	  		try {
	  
	  			//String query = "SELECT * FROM account_signals WHERE account_id="+ "'" +accountIdentifier +"'" +" ALLOW FILTERING";
	 			String query = "SELECT * FROM account_signals WHERE account_id= "+ "'" +accountIdentifier +"'"+" AND time>= "+fromTime+"AND time<= "+toTime+"  ALLOW FILTERING";
//	  			
//		  	            BuiltStatement builtStatement = QueryBuilder.select().all()
//		  	                    .from("account_signals")
//		  	                    .where(QueryBuilder.eq("account_id", accountIdentifier))
//		  	                    .and(QueryBuilder.gte("time", fromTime)).and(
//		  	                                       QueryBuilder.lte("time", toTime ));	
	  			
		  	           rows = cassandraTemplate.select(query, Row.class);
		  	        
		  	          
		              for (Row row : rows) {
		                  String signalSet = row.getString("signals");
		                  signalIds.add(signalSet);
		              }
		  	            return signalIds;
	  		} catch (DataAccessException e) {
	  			log.error("Error while fetching  Detail getSignal() of AccountSignalDao class ", e);
	  			throw new ServerException("Error while fetching  Detail getSignal() of AccountSignalDao class :"+e.getMessage() );
	  		}
	  	}
	    
	    public  List<Row> getSignalList(Set<String> signalId) throws ServerException {
	    	List<Row> result = null;
	  		try {
	 			String query = "SELECT * FROM signal_details WHERE signal_id= "+ "'" +signalId +"'"+" ALLOW FILTERING";

//	  	            BuiltStatement builtStatement = QueryBuilder.select().all()
//	  	                    .from("signal_details")
//	  	                    .where(QueryBuilder.eq("signal_id", signalId));
  	           result = cassandraTemplate.select(query, Row.class);
	  	            return  result;
	  		} catch (DataAccessException e) {
	  			log.error("Error while fetching  getSignalId getSignalList() of AccountSignalDao class ", e);
	  			throw new ServerException("Error while fetching  Detail getSignalList() of AccountSignalDao class : no data found for List of signalId"+signalId);
	  		}
	  	}
	    
	    public  List<Row> getServiceMaintenanceWindowList(String accountId, String serviceId) throws ServerException {
	    	List<Row> result = null;
	  		try {
	 			String query = "SELECT * FROM service_maintenance_data WHERE account_id = "+ "'" +accountId +"'" + " AND service_id = "+ "'" +serviceId +"'" + "  ALLOW FILTERING";

//	  			 BuiltStatement builtStatement = (QueryBuilder.select().all()
//	  	                .from("service_maintenance_data")
//	  	                .where(QueryBuilder.eq("account_id", accountId))
//	  	                .and(QueryBuilder.eq("service_id",serviceId)));	 	
	 			
	  	           result = cassandraTemplate.select(query, Row.class);
	  	            return  result;
	  		} catch (DataAccessException e) {
	  			log.error("Error while fetching ServiceMaintenanceWindowList in  getServiceMaintenanceWindowList() of AccountCassandraDao class ", e);
	  			throw new ServerException("Error while fetching  Detail getServiceMaintenanceWindowList() of AccountCassandraDao class : no data found for List of accountId"+accountId);
	  		}
	  	}
	    
	   
	   
}
