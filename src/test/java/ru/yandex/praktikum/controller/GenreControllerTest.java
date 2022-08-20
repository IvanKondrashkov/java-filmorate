package ru.yandex.praktikum.controller;

import org.mockito.Mockito;
import java.util.List;
import java.time.LocalDate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.praktikum.model.Genre;
import ru.yandex.praktikum.dao.genre.GenreStorage;
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
class GenreControllerTest {
    private Genre genre;
    private Gson gson;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GenreStorage genreStorage;


    @BeforeEach
    void init() {
        genre = new Genre(1L, "Комедия");

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .serializeNulls()
                .create();
    }

    @AfterEach
    void tearDown() {
        genre = null;
        gson = null;
        mockMvc = null;
    }

    @Test
    @DisplayName("Send GET request /genres/{id}")
    void findById() throws Exception {
        Mockito.when(genreStorage.findById(genre.getId())).thenReturn(genre);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/genres/{id}", genre.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber());
    }

    @Test
    @DisplayName("Send GET request /genres")
    void findAll() throws Exception {
        Mockito.when(genreStorage.findAll()).thenReturn(List.of(genre));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/genres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber());
    }

    @Test
    @DisplayName("Send POST request /genres")
    void save() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(genre)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Send DELETE request genres/{id}")
    void deleteById() throws Exception {
        Mockito.when(genreStorage.findById(genre.getId())).thenReturn(genre);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/genres/{id}", genre.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
