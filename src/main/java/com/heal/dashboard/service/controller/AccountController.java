package com.heal.dashboard.service.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.heal.dashboard.service.entities.AccountBean;
import com.heal.dashboard.service.entities.ApplicationDetailBean;
import com.heal.dashboard.service.entities.DateComponentDetailBean;
import com.heal.dashboard.service.entities.MasterFeatureDetails;
import com.heal.dashboard.service.entities.applicationhealth.ApplicationHealthDetail;
import com.heal.dashboard.service.entities.topology.TopologyDetails;
import com.heal.dashboard.service.service.GetAccountsService;
import com.heal.dashboard.service.service.TopologyDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(value = "Accounts")
public class AccountController {
	@Autowired
    GetAccountsService getAccountsService;
    @Autowired
    TopologyDetailsService topologyDetailsService;

    @ApiOperation(value = "Retrieve Account List", response = AccountBean.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved data"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 400, message = "Invalid Request")})
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ResponseEntity<List<AccountBean>> getAccountList(@RequestHeader(value = "Authorization") String auth) {
       List<AccountBean> response = getAccountsService.getAccountList(auth);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    
    @ApiOperation(value = "Retrieve applications List", response = ApplicationDetailBean.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved data"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 400, message = "Invalid Request")})
    @RequestMapping(value = "/accounts/{identifier}/applications", method = RequestMethod.GET)
    public ResponseEntity<List<ApplicationDetailBean>> getApplicationList(@RequestHeader(value = "Authorization") String auth,@PathVariable("identifier") String identifier) {
       List<ApplicationDetailBean> response = getAccountsService.getApplicationList(auth,identifier);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    
    @ApiOperation(value = "Retrieve Service Details", response = TopologyDetails.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved data"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 400, message = "Invalid Request")})
    @RequestMapping(value = "/accounts/{identifier}/services/{serviceId}/topology/{ndegree}", method = RequestMethod.GET)
    public ResponseEntity<TopologyDetails> getServiceDetails(@RequestHeader(value = "Authorization") String auth,@PathVariable("identifier") String identifier,@PathVariable("serviceId") String serviceId,@PathVariable("ndegree") String ndegree) {
       TopologyDetails response = topologyDetailsService.getServiceDetails(auth,identifier,serviceId,ndegree);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Retrieve TopologyDetails", response = TopologyDetails.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved data"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 400, message = "Invalid Request")})
    @RequestMapping(value = "/accounts/{identifier}/topology", method = RequestMethod.GET)
    public ResponseEntity<TopologyDetails> getTopologyDetails(@RequestHeader(value = "Authorization") String auth,@PathVariable("identifier") String identifier,@RequestParam(value ="applicationId",required=false) String applicationId) {
       TopologyDetails response = topologyDetailsService.getTopologyDetails(auth,identifier,applicationId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Retrieve features List", response = AccountBean.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved data"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 400, message = "Invalid Request")})
    @RequestMapping(value = "/features", method = RequestMethod.GET)
    public ResponseEntity<MasterFeatureDetails> getMasterFeatures() {
        MasterFeatureDetails response = getAccountsService.getMasterFeatures();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @ApiOperation(value = "Retrieve Date Component Drop Down List", response = AccountBean.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved data"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 400, message = "Invalid Request")})
    @RequestMapping(value = "/date-components", method = RequestMethod.GET)
    public ResponseEntity<DateComponentDetailBean> getDateTimeDropdownList() {
        DateComponentDetailBean response = getAccountsService.getDateTimeDropdownList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Retrieve Application Health", response = AccountBean.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved data"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 400, message = "Invalid Request")})
    @RequestMapping(value = "/accounts/{identifier}/application-health", method = RequestMethod.GET)
    public ResponseEntity< List<ApplicationHealthDetail>> getApplicationHealthStatus(@RequestHeader(value = "Authorization") String authorizationToken,@PathVariable(value = "identifier") String identifier,
                                                                              @RequestParam(value = "toTime") String toTimeString) {
        List<ApplicationHealthDetail> response = getAccountsService.getApplicationHealthStatus(identifier, toTimeString,authorizationToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
