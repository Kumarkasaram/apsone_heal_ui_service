package com.heal.uiservice.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UtilityBean<T> {

    T pojoObject;
    String accountIdentifier;
    String authToken;
}
