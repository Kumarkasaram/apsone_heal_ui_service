package com.heal.dashboard.service.businesslogic;



import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.cql.Row;
import com.heal.dashboard.service.dao.mysql.AccountCassandraDao;
import com.heal.dashboard.service.entities.AccountBean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Component
public class MaintainanceWindowsBL {

	@Autowired
	AccountCassandraDao accountCassandraDao; 
	
	public boolean getServiceMaintenanceStatus(AccountBean account, String serviceId, Timestamp toTime){
        try {
            List<Row> completedMaintenanceWindows = null;
            completedMaintenanceWindows = accountCassandraDao.getServiceMaintenanceWindowList(account.getIdentifier(), serviceId);
            if(!completedMaintenanceWindows.isEmpty())
                for(Row maintenanceWindow : completedMaintenanceWindows){
                    Timestamp startTimeMW = new Timestamp(maintenanceWindow.getLong("start_time"));
                    Timestamp endTimeMW = new Timestamp(maintenanceWindow.getLong("end_time"));
                    if(startTimeMW.before(toTime) && endTimeMW.after(toTime)){
                        return true;
                    }
                }
        } catch (Exception e) {
            log.info("Error Occurred while checking Maintenance For Service :{}", serviceId);
            return false;
        }
        return false;
    }
}

