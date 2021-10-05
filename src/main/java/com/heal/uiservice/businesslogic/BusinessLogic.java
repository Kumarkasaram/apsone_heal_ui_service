package com.heal.uiservice.businesslogic;

import com.heal.uiservice.entities.UtilityBean;
import com.heal.uiservice.exception.ClientException;
import com.heal.uiservice.exception.DataProcessingException;
import com.heal.uiservice.exception.ServerException;
import com.heal.uiservice.pojo.RequestObject;
import org.springframework.stereotype.Component;

@Component
public interface BusinessLogic<T, V, R> {
    UtilityBean<T> clientValidation(RequestObject requestObject) throws ClientException;
    V serverValidation(UtilityBean<T> utilityBean) throws ServerException;
    R process(V bean) throws DataProcessingException;
}
