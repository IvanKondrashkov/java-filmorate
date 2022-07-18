package ru.yandex.praktikum.validations;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.praktikum.model.Film;
import java.time.Month;
import java.time.LocalDate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.praktikum.utils.LocalDateAdapter;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmValidationTest {
    private Film film;
    private Gson gson;
    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void init() {
        film = new Film(1L, "Home Alone", "Rate 8.3", LocalDate.of(1990, Month.NOVEMBER, 10), 130);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .serializeNulls()
                .create();
    }

    @AfterEach
    void tearDown() {
        film = null;
        gson = null;
        mockMvc = null;
    }

    @Test
    @DisplayName("Film validation by valid credentials")
    void saveFilmByValidCredentials() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(film)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Film validation by null and empty name")
    void saveFilmByNullAndEmptyName(String name) throws Exception {
        film.setName(name);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(status().isInternalServerError());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 201, 202})
    @DisplayName("Film validation by invalid description")
    void saveFilmByInvalidDescription(int length) throws Exception {
        StringBuilder builder = new StringBuilder(film.getDescription());

        while (builder.length() < length) {
            builder.append(" ");
            builder.append(film.getDescription());
        }

        film.setDescription(builder.toString());
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Film validation by invalid release date")
    void saveFilmByInvalidReleaseDate() throws Exception {
        film.setReleaseDate(LocalDate.of(1880, Month.AUGUST, 12));
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(status().isInternalServerError());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -130})
    @DisplayName("Film validation by zero and negative duration")
    void saveFilmByZeroAndNegativeDuration(int duration) throws Exception {
        film.setDuration(duration);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(status().isInternalServerError());
    }
}
