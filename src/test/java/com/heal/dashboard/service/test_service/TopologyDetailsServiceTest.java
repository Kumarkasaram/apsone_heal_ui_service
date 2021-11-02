package com.heal.dashboard.service.test_service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import com.heal.dashboard.service.businesslogic.topology.ServiceDetailsBL;
import com.heal.dashboard.service.businesslogic.topology.TopologyServiceBL;
import com.heal.dashboard.service.entities.UtilityBean;
import com.heal.dashboard.service.entities.topology.Edges;
import com.heal.dashboard.service.entities.topology.Nodes;
import com.heal.dashboard.service.entities.topology.TopologyDetails;
import com.heal.dashboard.service.entities.topology.TopologyValidationResponseBean;
import com.heal.dashboard.service.service.TopologyDetailsService;

@RunWith(SpringRunner.class)
public class TopologyDetailsServiceTest {

	@InjectMocks
	TopologyDetailsService topologyDetailsService;
	
	@Mock
	ServiceDetailsBL serviceDetailsBL;
	@Mock
	TopologyServiceBL  topologyServiceBL;
	
	private  List<Nodes> nodeslist;
	private  List<Edges> edgeslist;
	private  TopologyDetails topologyDetails;
	 
	TopologyValidationResponseBean topologyResponse;
	
	  @Before
	    public void setup() {
//	      setting up mock data in  NodesList
	        nodeslist = new ArrayList<Nodes>();
	        edgeslist = new ArrayList<>();
	         
	         Nodes nodes = new Nodes();
	         nodes.setId("1");
	         nodes.setIdentifier("qa-d681ef13-d690-4917-jkhg-6c79b-1");
	         nodes.setName("test");
	         nodes.setStartNode(true);
	         nodeslist.add(nodes);
	         
//		     setting up mock data in  EdgesList
	         Edges edges = new Edges();
	         edges.setSource("test");
	         edges.setTarget("1");
	         edges.setData(new HashMap<String, String>());
	         edgeslist.add(edges);
	         
//	     	 setting up mock data in  topologyResponse
	         topologyResponse = new TopologyValidationResponseBean(nodeslist, edgeslist, "1");
	    	 topologyDetails = new TopologyDetails();
	    	 topologyDetails.setEdges(edgeslist);
	    	 topologyDetails.setNodes(nodeslist);
	    	
	    }	

		    @Test
		    public void getServiceDetails() throws Exception {
		        Mockito.when(serviceDetailsBL.clientValidation(Mockito.any())).thenReturn(UtilityBean.<String>builder().pojoObject("7640123a-fbde-4fe5-9812-581cd1e3a9c1").build());
		        Mockito.when(serviceDetailsBL.serverValidation(Mockito.any())).thenReturn(topologyResponse);
		        Mockito.when(serviceDetailsBL.process(Mockito.any())).thenReturn(topologyDetails);
		        Assert.assertEquals(true, topologyDetailsService.getServiceDetails("7640123a-fbde-4fe5-9812-581cd1e3a9c1","","","").getNodes().get(0).isStartNode());
		    }
		    
		    @Test
		    public void getTopologyDetails() throws Exception {
		        Mockito.when(topologyServiceBL.clientValidation(Mockito.any())).thenReturn(UtilityBean.<String>builder().pojoObject("7640123a-fbde-4fe5-9812-581cd1e3a9c1").build());
		        Mockito.when(topologyServiceBL.serverValidation(Mockito.any())).thenReturn(topologyResponse);
		        Mockito.when(topologyServiceBL.process(Mockito.any())).thenReturn(topologyDetails);
		        Assert.assertEquals(true, topologyDetailsService.getTopologyDetails("7640123a-fbde-4fe5-9812-581cd1e3a9c1","","").getNodes().get(0).isStartNode());
		    }
	
}
