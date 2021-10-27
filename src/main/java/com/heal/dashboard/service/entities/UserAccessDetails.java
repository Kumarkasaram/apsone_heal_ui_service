package com.heal.dashboard.service.entities;

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
