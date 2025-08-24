package com.thiagosalvess.booking.infrastructure.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureMockMvc
class UserControllerTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldCreateAnUserSuccessfuly() throws Exception {
        var payload = """
                {
                  "name" : "João  Ribeiro"
                }
                """;

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.message").value("User created successfully"))
                    .andExpect(jsonPath("$.user.id").isNotEmpty())
                    .andExpect(jsonPath("$.user.name").isNotEmpty());

    }

    @Test
    public void shouldReturn400IfNameIsEmpty() throws Exception {
        var payload = """
                {
                  "name" : ""
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("O nome é obrigatório!"));

    }
}