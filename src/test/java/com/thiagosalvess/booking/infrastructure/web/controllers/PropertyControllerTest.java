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
class PropertyControllerTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    MockMvc mockMvc;


    @Test
    public void shouldCreatePropertySuccessfuly() throws Exception {
        var payload = """
                {
                  "name" : "Apartamento no centro",
                  "description": "Apartamento 3 quartos no centro",
                  "maxGuests": 5,
                  "basePricePerNight": 100.0
                }
                """;

        mockMvc.perform(post("/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Property created successfully"))
                .andExpect(jsonPath("$.property.id").isNotEmpty())
                .andExpect(jsonPath("$.property.name").isNotEmpty())
                .andExpect(jsonPath("$.property.description").isNotEmpty())
                .andExpect(jsonPath("$.property.maxGuests").isNumber())
                .andExpect(jsonPath("$.property.basePricePerNight").isNumber());
    }

    @Test
    public void shouldReturn400IfNameIsEmpty() throws Exception {
        var payload = """
                {
                  "name" : "",
                  "description": "Apartamento 3 quartos no centro",
                  "maxGuests": 5,
                  "basePricePerNight": 100.0
                }
                """;

        mockMvc.perform(post("/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("O nome é obrigatório!"));

    }

    @Test
    public void shouldReturn400IfMaxGuestIsEqualOrLessThenZero() throws Exception {
        var payload = """
                {
                  "name" : "Apartamento 3 quartos no centro",
                  "description": "Apartamento 3 quartos no centro",
                  "maxGuests": 0,
                  "basePricePerNight": 100.0
                }
                """;

        mockMvc.perform(post("/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("O número máximo de hóspedes deve ser maior que zero!"));
    }

    @Test
    public void shouldReturn400IfBasePricePerNightIsMissing() throws Exception {
        var payload = """
                {
                  "name" : "Apartamento 3 quartos no centro",
                  "description": "Apartamento 3 quartos no centro",
                  "maxGuests": 3
                }
                """;

        mockMvc.perform(post("/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("O preço base por noite é obrigatório!"));
    }
}