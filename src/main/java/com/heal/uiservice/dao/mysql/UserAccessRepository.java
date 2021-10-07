package com.heal.uiservice.dao.mysql;

import com.heal.uiservice.entities.BaseEntity;
import com.heal.uiservice.entities.UserAccessDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.websocket.server.PathParam;
import java.io.Serializable;

@Repository
public interface UserAccessRepository<T extends BaseEntity, E extends Serializable> extends JpaRepository<T, E> {

  /**  @Query("select new com.heal.uiservice.entities.UserAccessDetails(a.accessDetails, a.userIdentifier) from UserAccessDetails a where " +
            "userIdentifier=:userIdentifier")
    UserAccessDetails fetchUserAccessDetailsUsingIdentifier(@PathParam("userIdentifier") String userIdentifier);**/
}
