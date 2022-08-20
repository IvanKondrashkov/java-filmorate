package ru.yandex.praktikum.controller;

import org.mockito.Mockito;
import java.util.List;
import java.time.LocalDate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.praktikum.model.Mpa;
import ru.yandex.praktikum.dao.mpa.MpaStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.praktikum.utils.LocalDateAdapter;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MpaControllerTest {
    private Mpa mpa;
    private Gson gson;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MpaStorage mpaStorage;


    @BeforeEach
    void init() {
        mpa = new Mpa(1L, "G");

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .serializeNulls()
                .create();
    }

    @AfterEach
    void tearDown() {
        mpa = null;
        gson = null;
        mockMvc = null;
    }

    @Test
    @DisplayName("Send GET request /mpa/{id}")
    void findById() throws Exception {
        Mockito.when(mpaStorage.findById(mpa.getId())).thenReturn(mpa);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/mpa/{id}", mpa.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber());
    }

    @Test
    @DisplayName("Send GET request /mpa")
    void findAll() throws Exception {
        Mockito.when(mpaStorage.findAll()).thenReturn(List.of(mpa));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/mpa")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber());
    }

    @Test
    @DisplayName("Send POST request /mpa")
    void save() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/mpa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(mpa)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Send DELETE request mpa/{id}")
    void deleteById() throws Exception {
        Mockito.when(mpaStorage.findById(mpa.getId())).thenReturn(mpa);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/mpa/{id}", mpa.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
