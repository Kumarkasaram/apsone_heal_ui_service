package com.heal.uiservice.dao.mysql;

import com.heal.uiservice.entities.Account;
import com.heal.uiservice.entities.AccountBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.websocket.server.PathParam;
import java.io.Serializable;
import java.util.List;

@Repository
public interface AccountRepository<T extends Account, E extends Serializable> extends JpaRepository<T, E> {

    @Query("select new com.heal.uiservice.entities.AccountBean(a.id, a.status, mt.timeoffset, mt.timeoffset, a.name, a.identifier, " +
            "mt.timeZoneId, a.updatedTime, a.userDetailsId, mt.abbreviation, mt.offsetName, a.userDetailsId, a.updatedTime, a.createdTime) " +
            "FROM Account a join TagMapping as tm on tm.objectId = a.id join TagDetails as td on tm.tagId = td.id " +
            "join MstTimezone as mt on mt.id = tm.tagKey where tm.objectRefTable =:accountTableName and td.name = :timezoneKey and a.status = 1")
    List<AccountBean> getAccountDetails(@PathParam("timezoneKey") String timezoneKey, @PathParam("accountTableName") String accountTableName);
}
