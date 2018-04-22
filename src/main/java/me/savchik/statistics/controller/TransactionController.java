package me.savchik.statistics.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import me.savchik.statistics.dto.TransactionCreateRequest;
import me.savchik.statistics.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/transactions")
@Validated
public class TransactionController {

    private TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created successfully"),
            @ApiResponse(code = 204, message = "Transaction is older than 60 seconds"),
            @ApiResponse(code = 400, message = "Validation failed")})
    public void create(@Valid @RequestBody TransactionCreateRequest request, HttpServletResponse response) {
        HttpStatus status = service.addTransaction(request) ?
                HttpStatus.CREATED :
                HttpStatus.NO_CONTENT;
        response.setStatus(status.value());
    }

}
