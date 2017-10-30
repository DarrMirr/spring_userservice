package com.github.darrmirr.controller;

import org.junit.*;
import org.junit.runner.RunWith;
import com.github.darrmirr.config.UserAppConfig;
import com.github.darrmirr.config.UserAppDataConfigTest;
import com.github.darrmirr.exceptions.UserNotFoundException;
import com.github.darrmirr.model.UserStatus;
import com.github.darrmirr.model.UserStatusEnum;
import com.github.darrmirr.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.github.darrmirr.model.UserStatusEnum.ONLINE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;

/*
 * @author Darr Mirr
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UserAppConfig.class, UserAppDataConfigTest.class})
@WebAppConfiguration
@ActiveProfiles("test")
public class UserControllerTests {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private Environment environment;

    private MockMvc mockMvc;
    private RedisServer redisServer;

    private static final String USER_ID_INVALID = "12345678";
    private static final String USER_ID = "1";
    private static final String CREATE_USER_BODY = "{\"username\":\"Vinny Puh\",\"email\":\"vinny_puh@domain.net\",\"phone\":\"9001234560\"}";
    private static final String CHANGE_STATUS_BODY = "{\"userStatus\":\"online\"}";

    @After
    public  void classTearDown() throws InterruptedException {
        redisServer.stop();
    }

    @Before
    public void setUp() throws IOException, URISyntaxException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        redisServer = new RedisServer(Integer.parseInt(environment.getProperty("redis.port")));
        redisServer.start();
    }

    @Test
    public void userInfoResponseValidation() throws Exception {
        mockMvc.perform(get("/users/{id}", USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.username").isNotEmpty())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andExpect(jsonPath("$.phone").exists())
                .andExpect(jsonPath("$.phone").isNotEmpty());
    }

    @Test
    public void userNotFoundResponseValidation() throws Exception {
        mockMvc.perform(get("/users/{id}", USER_ID_INVALID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0].name").value(UserNotFoundException.class.getSimpleName()))
                .andExpect(jsonPath("$.errors[0].description").exists())
                .andExpect(jsonPath("$.errors[0].description").value(containsString(USER_ID_INVALID)));
    }

    @Test
    public void userStatusResponseValidation() throws Exception {
        userStatusRepository.save(new UserStatus(Long.parseLong(USER_ID), ONLINE));
        mockMvc.perform(get("/users/{id}/status", USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(Long.parseLong(USER_ID)))
                .andExpect(jsonPath("$.userStatus").exists())
                .andExpect(jsonPath("$.userStatus").value(is(ONLINE.getName())));
    }

    @Test
    public void userStatusNotFountResponseValidation() throws Exception {
        mockMvc.perform(get("/users/{id}/status", USER_ID_INVALID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0].name").value(UserNotFoundException.class.getSimpleName()))
                .andExpect(jsonPath("$.errors[0].description").exists())
                .andExpect(jsonPath("$.errors[0].description").value(containsString(USER_ID_INVALID)));
    }

    @Test
    public void createUserResponseValidation() throws Exception {
        mockMvc.perform(post("/users").content(CREATE_USER_BODY).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void changeStatusResponseValidation() throws Exception {
        mockMvc.perform(post("/users/{id}/status", USER_ID).content(CHANGE_STATUS_BODY).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(is(Integer.parseInt(USER_ID))))
                .andExpect(jsonPath("$.newStatus").exists())
                .andExpect(jsonPath("$.newStatus").isNotEmpty())
                .andExpect(jsonPath("$.oldStatus").exists())
                .andExpect(jsonPath("$.oldStatus").isNotEmpty());
    }

    @Test
    public void changeStatusAwaySchedule() throws Exception {
        int timeout = 1;
        ONLINE.setDuration(Duration.ofSeconds(timeout));
        mockMvc.perform(post("/users/{id}/status", USER_ID).content(CHANGE_STATUS_BODY).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        TimeUnit.SECONDS.sleep(timeout + 1);
        mockMvc.perform(get("/users/{id}/status", USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatus").value(UserStatusEnum.AWAY.getName()));
    }
}
