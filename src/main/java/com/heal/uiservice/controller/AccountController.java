package com.heal.uiservice.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.heal.uiservice.entities.AccountBean;
import com.heal.uiservice.service.GetAccountsService;
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

    @ApiOperation(value = "Retrieve Account List", response = AccountBean.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved data"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 400, message = "Invalid Request")})
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ResponseEntity<List<AccountBean>> getAccountList(@RequestHeader(value = "Authorization") String auth) {
       List<AccountBean> response = getAccountsService.getAccountList(auth);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
