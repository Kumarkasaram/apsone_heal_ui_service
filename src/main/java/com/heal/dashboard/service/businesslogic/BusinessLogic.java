package com.heal.dashboard.service.businesslogic;

import com.heal.dashboard.service.exception.ClientException;
import com.heal.dashboard.service.exception.DataProcessingException;
import com.heal.dashboard.service.exception.ServerException;
import com.heal.dashboard.service.entities.UtilityBean;
import com.heal.dashboard.service.pojo.RequestObject;
import org.springframework.stereotype.Component;

@Component
public interface BusinessLogic<T, V, R> {
    UtilityBean<T> clientValidation(RequestObject<T> requestObject) throws ClientException;
    V serverValidation(UtilityBean<T> utilityBean) throws ServerException;
    R process(V bean) throws DataProcessingException;
}
