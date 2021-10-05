package com.heal.uiservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TagMapping extends BaseEntity {

    private int tagId;
    private int objectId;
    private String objectRefTable;
    private String tagKey;
    private String tagValue;
    private int accountId;
    private String userDetailsId;

}
