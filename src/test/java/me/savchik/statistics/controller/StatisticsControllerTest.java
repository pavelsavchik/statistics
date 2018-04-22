package me.savchik.statistics.controller;

import me.savchik.statistics.entity.Statistics;
import me.savchik.statistics.service.StatisticsService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StatisticsService statisticsService;

    @Test
    public void GET_statisticsWasCalculated_valuesAreCorrect() throws Exception {
        Statistics statistics = new Statistics(10.5, 5D, 5D, 3.2, 3L);
        given(statisticsService.getStatistics()).willReturn(statistics);

        mvc.perform(get("/statistics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.sum", Matchers.is(10.5)))
                .andExpect(jsonPath("$.avg", Matchers.is(5.0)))
                .andExpect(jsonPath("$.max", Matchers.is(5.0)))
                .andExpect(jsonPath("$.min", Matchers.is(3.2)))
                .andExpect(jsonPath("$.count", Matchers.is(3)));
    }

    @Test
    public void GET_statisticsIsEmpty_valuesAreZero() throws Exception {
        Statistics statistics = new Statistics();
        given(statisticsService.getStatistics()).willReturn(statistics);

        mvc.perform(get("/statistics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.sum", Matchers.is(0.0)))
                .andExpect(jsonPath("$.avg", Matchers.is(0.0)))
                .andExpect(jsonPath("$.max", Matchers.is(0.0)))
                .andExpect(jsonPath("$.min", Matchers.is(0.0)))
                .andExpect(jsonPath("$.count", Matchers.is(0)));
    }

}
