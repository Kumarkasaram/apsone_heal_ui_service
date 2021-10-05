package com.heal.uiservice.controller;

import com.heal.uiservice.entities.AccountBean;
import com.heal.uiservice.pojo.ResponseBean;
import com.heal.uiservice.service.GetAccountsService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(value = "Accounts")
public class AccountController {
    @Autowired
    GetAccountsService getAccountsService;

    @ApiOperation(value = "Retrieve Account List", response = ResponseBean.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved data"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 400, message = "Invalid Request")})
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ResponseEntity<ResponseBean<List<AccountBean>>> getAccountList(@ApiParam(value = "Api key", required = true)
                                                                          @RequestHeader(value = "Authorization") String auth) {
        ResponseBean<List<AccountBean>> response = getAccountsService.getAccountList(auth);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
