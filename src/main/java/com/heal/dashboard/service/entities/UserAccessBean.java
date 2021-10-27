package com.heal.dashboard.service.entities;



import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccessBean extends BaseEntity {

    private String accessDetails;
    private String userIdentifier;
}
