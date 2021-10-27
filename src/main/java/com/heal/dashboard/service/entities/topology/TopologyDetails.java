package com.heal.dashboard.service.entities.topology;



import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TopologyDetails {
	 List<String> impactedServiceName = new ArrayList();
     List<Nodes> nodes;
     List<Edges> edges;
}

