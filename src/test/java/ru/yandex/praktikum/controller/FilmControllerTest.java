package ru.yandex.praktikum.controller;

import org.mockito.Mockito;
import java.util.List;
import java.util.Random;
import java.time.Month;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.praktikum.dao.film.FilmStorage;
import ru.yandex.praktikum.model.Genre;
import ru.yandex.praktikum.model.Mpa;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.Film;
import ru.yandex.praktikum.dao.user.UserStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.praktikum.exception.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.praktikum.utils.LocalDateAdapter;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    private Film film;
    private User user;
    private Gson gson;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserStorage userStorage;
    @MockBean
    private FilmStorage filmStorage;


    @BeforeEach
    void init() {
        film = new Film(1L
                , "Home Alone"
                , "Rate 8.3"
                , LocalDate.of(1990, Month.NOVEMBER, 10)
                , 130, new Mpa(1L,"G"),
                new LinkedHashSet<>());
        film.getGenres().add(new Genre(1L, "Комедия"));

        user = new User(10L
                , "Jon"
                , "dev-java"
                , "develop@mail.ru"
                , LocalDate.of(1992, Month.JANUARY, 14));

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .serializeNulls()
                .create();
    }

    @AfterEach
    void tearDown() {
        film = null;
        user = null;
        gson = null;
        mockMvc = null;
    }

    @Test
    @DisplayName("Send GET request /films/{id}")
    void findById() throws Exception {
        Mockito.when(filmStorage.findById(film.getId())).thenReturn(film);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/films/{id}", film.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber());
    }

    @Test
    @DisplayName("Send GET request /films/{id}")
    void findByRandomId() throws Exception {
        Mockito.when(filmStorage.findById(film.getId())).thenReturn(film);

        film.setId(new Random().nextLong());
        String message = String.format("Film with id=%d not found!", film.getId());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/films/{id}", film.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Send GET request /films")
    void findAll() throws Exception {
        Mockito.when(filmStorage.findAll()).thenReturn(List.of(film));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber());
    }

    @Test
    @DisplayName("Send GET request /films/popular")
    void findPopularFilms() throws Exception {
        Mockito.when(filmStorage.findById(film.getId())).thenReturn(film);
        Mockito.when(userStorage.findById(user.getId())).thenReturn(user);
        Mockito.when(filmStorage.findPopularFilms(10)).thenReturn(List.of(film));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/films/popular")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
        Mockito.when(filmStorage.update(film)).thenReturn(film);
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
        Mockito.when(filmStorage.findById(film.getId())).thenReturn(film);

        String message = "Film with not found!";
        film.setId(new Random().nextLong());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Send POST request films/{id}/like/{userId}")
    void addLike() throws Exception {
        Mockito.when(filmStorage.findById(film.getId())).thenReturn(film);
        Mockito.when(userStorage.findById(user.getId())).thenReturn(user);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/films/{id}/like/{userId}", film.getId(), user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Send DELETE request films/{id}/like/{userId}")
    void deleteLike() throws Exception {
        Mockito.when(filmStorage.findById(film.getId())).thenReturn(film);
        Mockito.when(userStorage.findById(user.getId())).thenReturn(user);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/films/{id}/like/{userId}", film.getId(), user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Send DELETE request films/{id}")
    void deleteById() throws Exception {
        Mockito.when(filmStorage.findById(film.getId())).thenReturn(film);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/films/{id}", film.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
