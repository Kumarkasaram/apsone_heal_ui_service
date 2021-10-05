package com.heal.uiservice.entities;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserAccessDetails extends BaseEntity {

    private String accessDetails;
    private String userIdentifier;
}
