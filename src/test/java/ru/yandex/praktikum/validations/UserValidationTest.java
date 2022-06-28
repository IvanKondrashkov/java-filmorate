package ru.yandex.praktikum.validations;

import ru.yandex.praktikum.model.User;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.praktikum.controller.UserController;
import ru.yandex.praktikum.utils.LocalDateAdapter;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserValidationTest {
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
    @DisplayName("User validation by valid credentials")
    void saveUserByValidCredentials() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "develop$mail.ru"})
    @DisplayName("User validation by invalid email")
    void saveUserByInvalidEmail(String email) throws Exception {
        user.setEmail(email);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("User validation by null and empty login")
    void saveUserByNullAndEmptyLogin(String login) throws Exception {
        user.setLogin(login);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("User validation by null and empty name")
    void saveUserByNullAndEmptyName(String name) throws Exception {
        user.setName(name);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("dev-java"));
    }

    @Test
    @DisplayName("User validation by invalid birthday")
    void saveUserByInvalidBirthday() throws Exception {
        user.setBirthday(LocalDate.of(2023, Month.AUGUST, 12));
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isBadRequest());
    }
}
