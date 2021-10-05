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
public class Account extends BaseEntity {
    private String name;
    private int status;
    private String privateKey;
    private String publicKey;
    private String userDetailsId;
    private String identifier;
}