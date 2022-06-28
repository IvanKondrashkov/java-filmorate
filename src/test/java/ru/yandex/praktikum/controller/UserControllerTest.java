package ru.yandex.praktikum.controller;

import ru.yandex.praktikum.model.User;
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

@WebMvcTest(UserController.class)
class UserControllerTest {
    private User user;
    private Gson gson;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    void init() {
        user = new User(1L, "develop@mail.ru", "dev-java", "Jon", LocalDate.of(1992, Month.JANUARY, 14));
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
        user = null;
        gson = null;
        mockMvc = null;
    }

    @Test
    @DisplayName("Send GET request /users")
    void findAll() throws Exception {
        save();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber());
    }

    @Test
    @DisplayName("Send POST request /users")
    void save() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Send PUT request /users")
    void update() throws Exception {
        save();
        user.setName("Mike");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mike"));
    }

    @Test
    @DisplayName("Send PUT request /users by random id")
    void updateByRandomId() throws Exception {
        final String message = "A user with this id was not found!";
        save();
        user.setId(new Random().nextLong());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
    }
}
