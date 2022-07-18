package ru.yandex.praktikum.controller;

import org.mockito.Mockito;
import java.util.List;
import java.util.Random;
import java.time.Month;
import java.time.LocalDate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.praktikum.model.User;
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
class UserControllerTest {
    private User user;
    private User friend;
    private Gson gson;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserStorage userStorage;


    @BeforeEach
    void init() {
        user = new User(1L, "develop@mail.ru", "dev-java", "Jon", LocalDate.of(1992, Month.JANUARY, 14));
        friend = new User(2L, "qa@mail.ru", "qa-java", "Mike", LocalDate.of(1988, Month.AUGUST, 9));

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .serializeNulls()
                .create();
    }

    @AfterEach
    void tearDown() {
        user = null;
        friend = null;
        gson = null;
        mockMvc = null;
    }

    @Test
    @DisplayName("Send GET request /users/{id}")
    void findById() throws Exception {
        Mockito.when(userStorage.findById(user.getId())).thenReturn(user);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber());
    }

    @Test
    @DisplayName("Send GET request /users/{id}")
    void findByRandomId() throws Exception {
        Mockito.when(userStorage.findById(user.getId())).thenReturn(user);

        user.setId(new Random().nextLong());
        String message = String.format("User with id=%s not found!", user.getId());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Send GET request /users")
    void findAll() throws Exception {
        Mockito.when(userStorage.findAll()).thenReturn(List.of(user));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber());
    }

    @Test
    @DisplayName("Send GET request /users/{id}/friends")
    void findAllFriends() throws Exception {
        Mockito.when(userStorage.findById(user.getId())).thenReturn(user);
        Mockito.when(userStorage.findAllFriends(user.getId())).thenReturn(List.of(friend));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/{id}/friends", user.getId())
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
        Mockito.when(userStorage.update(user)).thenReturn(user);

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
        Mockito.when(userStorage.findById(user.getId())).thenReturn(user);

        String message = "User with not found!";
        user.setId(new Random().nextLong());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Send POST request users/{id}/friends/{friendId}")
    void addFriend() throws Exception {
        Mockito.when(userStorage.findById(user.getId())).thenReturn(user);
        Mockito.when(userStorage.findById(friend.getId())).thenReturn(friend);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/{id}/friends/{friendId}", user.getId(), friend.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Send DELETE request users/{id}/friends/{friendId}")
    void deleteFriend() throws Exception {
        Mockito.when(userStorage.findById(user.getId())).thenReturn(user);
        Mockito.when(userStorage.findById(friend.getId())).thenReturn(friend);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/{id}/friends/{friendId}", user.getId(), friend.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
