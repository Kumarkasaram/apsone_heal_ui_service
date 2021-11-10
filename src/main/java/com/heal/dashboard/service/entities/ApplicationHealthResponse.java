package com.heal.dashboard.service.entities;

import java.util.List;

import com.heal.dashboard.service.entities.applicationhealth.ApplicationHealthDetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ApplicationHealthResponse {
	
	List<TagMapping> tags = null;
	List<ApplicationHealthDetail> appHealthData =null;
	List<Controller> accessibleApps =null;

}
