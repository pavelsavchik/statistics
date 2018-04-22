package me.savchik.statistics.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import me.savchik.statistics.dto.TransactionCreateRequest;
import me.savchik.statistics.mapper.TransactionMapper;
import me.savchik.statistics.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/transactions")
@Validated
public class TransactionController {

    private final TransactionMapper mapper;

    private final TransactionRepository repository;

    @Autowired
    public TransactionController(TransactionMapper mapper, TransactionRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created successfully"),
            @ApiResponse(code = 204, message = "Transaction is older than 60 seconds"),
            @ApiResponse(code = 400, message = "Validation failed")})
    public void create(@Valid @RequestBody TransactionCreateRequest request, HttpServletResponse response) {
        HttpStatus status = repository.addTransaction(mapper.requestToTransaction(request)) ?
                HttpStatus.CREATED :
                HttpStatus.NO_CONTENT;
        response.setStatus(status.value());
    }

}
