package me.savchik.statistics.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.savchik.statistics.dto.TransactionCreateRequest;
import me.savchik.statistics.entity.Transaction;
import me.savchik.statistics.mapper.TransactionMapper;
import me.savchik.statistics.repository.TransactionRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public TransactionMapper transactionMapper() {
            return new TransactionMapper();
        }
    }

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionRepository transactionRepository;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void createWithActualTimestamp_201withEmptyBody() throws Exception {
        given(transactionRepository.addTransaction(any(Transaction.class))).willReturn(true);

        mvc.perform(post("/transactions")
                          .content(transactionJson())
                          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().is(201))
          .andExpect(content().bytes(new byte[0]));
    }

    @Test
    public void createWithExpiredTimestamp_204withEmptyBody() throws Exception {
        given(transactionRepository.addTransaction(any(Transaction.class))).willReturn(false);

        mvc.perform(post("/transactions")
                .content(transactionJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204))
                .andExpect(content().bytes(new byte[0]));
    }

    @Test
    public void createWithEmptyJson_404() throws Exception {
        mvc.perform(post("/transactions")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.fieldErrors", Matchers.hasSize(2)))
                .andExpect(jsonPath(fieldError("amount", "must not be null")).exists())
                .andExpect(jsonPath(fieldError("timestamp", "must not be null")).exists());
    }

    @Test
    public void createWithoutAmount_404() throws Exception {
        mvc.perform(post("/transactions")
                .content(transactionJson(null))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.fieldErrors", Matchers.hasSize(1)))
                .andExpect(jsonPath(fieldError("amount", "must not be null")).exists());
    }

    @Test
    public void createWithoutTimestamp_404() throws Exception {
        mvc.perform(post("/transactions")
                .content(transactionJson(10D, null))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.fieldErrors", Matchers.hasSize(1)))
                .andExpect(jsonPath(fieldError("timestamp", "must not be null")).exists());
    }

    @Test
    public void createWithIncorrectAmount_404() throws Exception {
        mvc.perform(post("/transactions")
                .content(transactionJson(10D).replace("10", "\"somestring\""))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.fieldErrors", Matchers.hasSize(1)))
                .andExpect(jsonPath(fieldError("amount", "must not be null")).exists());
    }

    private String transactionJson() throws JsonProcessingException {
        return transactionJson(10.);
    }

    private String transactionJson(Double amount) throws JsonProcessingException {
        TransactionCreateRequest request = new TransactionCreateRequest(amount, System.currentTimeMillis());
        return objectMapper.writeValueAsString(request);
    }

    private String transactionJson(Double amount, Long timestamp) throws JsonProcessingException {
        TransactionCreateRequest request = new TransactionCreateRequest(amount, timestamp);
        return objectMapper.writeValueAsString(request);
    }

    private String fieldError(String field, String message){
        return "$.fieldErrors[?(@.path == '" + field + "' && @.message == '" + message + "')]";
    }


}
