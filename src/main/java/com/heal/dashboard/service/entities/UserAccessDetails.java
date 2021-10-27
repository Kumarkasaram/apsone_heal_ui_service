package com.heal.dashboard.service.entities;

import lombok.*;

import java.util.List;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserAccessDetails extends BaseEntity {

	private List<String> applicationIdentifiers;
    private List<Integer> applicationIds;
    private List<Integer> serviceIds;
    private List<String> serviceIdentifiers;
    private List<Integer> transactionIds;
}
