package me.savchik.statistics.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import me.savchik.statistics.dto.TransactionCreateRequest;
import me.savchik.statistics.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created successfully"),
            @ApiResponse(code = 204, message = "Transaction is older than 60 seconds"),
            @ApiResponse(code = 400, message = "Validation failed")})
    public void create(@RequestBody TransactionCreateRequest request, HttpServletResponse response) {
        HttpStatus status = transactionService.create(request) ?
                HttpStatus.NO_CONTENT :
                HttpStatus.CREATED;
        response.setStatus(status.value());
    }

}
