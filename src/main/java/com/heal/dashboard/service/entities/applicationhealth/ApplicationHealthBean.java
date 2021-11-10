package com.heal.dashboard.service.entities.applicationhealth;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ApplicationHealthBean {

    private int id;
    private String identifier;
    private String name;
    private boolean maintenanceWindowStatus;
    private String dashboardUId;
    private int severeProblemCount;
    private int severeWarningCount;
    private int defaultProblemCount;
    private int defaultWarningCount;
    private int severeBatchCount;
    private int defaultBatchCount;
    private int severeProblemCountMaintenance;
    private int severeWarningCountMaintenance;
    private int defaultProblemCountMaintenance;
    private int defaultWarningCountMaintenance;
    private int severeBatchCountMaintenance;
    private int defaultBatchCountMaintenance;

    private ArrayList<ApplicationHealthStatus> problem = new ArrayList<>();
    private ArrayList<ApplicationHealthStatus> warning = new ArrayList<>();
    private ArrayList<ApplicationHealthStatus> batch = new ArrayList<>();
}
