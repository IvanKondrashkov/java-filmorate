package ru.yandex.praktikum.controller;

import ru.yandex.praktikum.model.Film;
import java.util.Random;
import java.time.Month;
import java.time.LocalDate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.praktikum.utils.LocalDateAdapter;
import ru.yandex.praktikum.exception.ValidationException;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(FilmController.class)
class FilmControllerTest {
    private Film film;
    private Gson gson;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    void init() {
        film = new Film(1L, "Home Alone", "Rate 8.3", LocalDate.of(1990, Month.NOVEMBER, 10), 130);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .serializeNulls()
                .create();

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .build();
    }

    @AfterEach
    void tearDown() {
        film = null;
        gson = null;
        mockMvc = null;
    }

    @Test
    @DisplayName("Send GET request /films")
    void findAll() throws Exception {
        save();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber());
    }

    @Test
    @DisplayName("Send POST request /films")
    void save() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Send PUT request /films")
    void update() throws Exception {
        save();
        film.setName("Titanic");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Titanic"));
    }

    @Test
    @DisplayName("Send PUT request /films by random id")
    void updateByRandomId() throws Exception {
        final String message = "A film with this id was not found!";
        save();
        film.setId(new Random().nextLong());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
    }
}
