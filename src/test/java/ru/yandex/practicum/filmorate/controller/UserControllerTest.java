/*package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.InvalidBirthDateException;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.InvalidLoginException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @SpyBean
    private UserController userController;
    private final static LocalDate BIRTHDAY = LocalDate.of(1967, 03, 25);
    private final static LocalDate BIRTHDAY_IN_FUTURE = LocalDate.of(2025, 03, 25);

    @Test
    void tryToCreateAndUpdateUserWithCorrectData() throws Exception {
        User user = new User(1, "solntmore@yandex.ru", RandomString.make(10),
                RandomString.make(10), BIRTHDAY);
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        user = new User(3, "solntmore2@yandex.ru", RandomString.make(10),
                RandomString.make(10), BIRTHDAY);
        mockMvc.perform(
                        put("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void tryToCreateUserWithInvalidEmailBadRequest() throws Exception {
        User user = new User(1, "левыйЕмаил", "login", "name", BIRTHDAY);

        this.mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof InvalidEmailException));
    }

    @Test
    void tryToCreateUserWithEmptyLoginBadRequest() throws Exception {
        User user = new User(1, "solntmore@yandex.ru", "", "name", BIRTHDAY);

        this.mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof InvalidLoginException));
    }

    @Test
    void tryToCreateUserWithEmptyNameBadRequest() throws Exception {
        User user = new User(1, "solntmore@yandex.ru", "name", "", BIRTHDAY);

        this.mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void tryToCreateUserWithIncorrectBirthdayBadRequest() throws Exception {
        User user = new User(1, "solntmore@yandex.ru", "login", "name", BIRTHDAY_IN_FUTURE);

        this.mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidBirthDateException))
                .andExpect(result -> assertEquals("Дата рождения не может быть в будущем.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void tryToUpdateUserWithInCorrectData() throws Exception {
        User user = new User(1, "solntmore@yandex.ru", RandomString.make(10),
                RandomString.make(10), BIRTHDAY);
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        user = new User(3, "solntmore2@yandex.ru", RandomString.make(10),
                RandomString.make(10), BIRTHDAY_IN_FUTURE);
        mockMvc.perform(
                        put("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidBirthDateException))
                .andExpect(result -> assertEquals("Дата рождения не может быть в будущем.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void tryTofindAll() throws Exception {
        User user1 = new User(1, "solntmore@yandex.ru", RandomString.make(10),
                RandomString.make(10), BIRTHDAY);
        User user2 = new User(2, "solntmore@yandex.ru", RandomString.make(10),
                RandomString.make(10), BIRTHDAY);
        User user3 = new User(3, "solntmore@yandex.ru", RandomString.make(10),
                RandomString.make(10), BIRTHDAY);
        Mockito.when(userController.findAllUsers()).thenReturn(Arrays.asList(user1, user2, user3));
        mockMvc.perform(
                        get("/users")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString
                        (Arrays.asList(user1, user2, user3))));
    }

}
*/