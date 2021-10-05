package com.heal.uiservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountBean extends BaseEntity {
    private int accountId;
    private int status;
    private int timezoneMilli;
    private int timeOffset;
    private String name;
    private String identifier;
    private String timeZoneString;
    private LocalDateTime updatedTimeString;
    private String updatedBy;
    private String abbreviation;
    private String offsetName;
    private String userDetailsId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}