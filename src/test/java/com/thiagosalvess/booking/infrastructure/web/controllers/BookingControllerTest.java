package com.thiagosalvess.booking.infrastructure.web.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiagosalvess.booking.infrastructure.persistence.entities.PropertyEntity;
import com.thiagosalvess.booking.infrastructure.persistence.entities.UserEntity;
import com.thiagosalvess.booking.infrastructure.repositories.SpringDataBookingJpa;
import com.thiagosalvess.booking.infrastructure.repositories.SpringDataPropertyJpa;
import com.thiagosalvess.booking.infrastructure.repositories.SpringDataUserJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookingControllerTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    MockMvc mockMvc;
    @Autowired
    SpringDataPropertyJpa propertyJpa;
    @Autowired
    SpringDataUserJpa userJpa;
    @Autowired
    SpringDataBookingJpa bookingJpa;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        bookingJpa.deleteAll();
        userJpa.deleteAll();
        propertyJpa.deleteAll();

        var uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var property = new PropertyEntity();
        property.setId(uuid);
        property.setName("Propriedade de Teste");
        property.setDescription("Um lugar incrível para ficar");
        property.setMaxGuests(5);
        property.setBasePricePerNight(100.0);
        propertyJpa.save(property);

        var user = new UserEntity();
        user.setId(uuid);
        user.setName("Usuário de Teste");
        userJpa.save(user);
    }

    @Test
    public void shouldCreateABookingSuccessfully() throws Exception {
        var payload = """
                {
                  "propertyId": "11111111-1111-1111-1111-111111111111",
                  "guestId": "11111111-1111-1111-1111-111111111111",
                  "startDate": "2024-12-20",
                  "endDate": "2024-12-25",
                  "guestCount": 2
                }
                """;

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Booking created successfully"))
                .andExpect(jsonPath("$.booking.id").isNotEmpty())
                .andExpect(jsonPath("$.booking.totalPrice").isNumber());
    }

    @Test
    public void shouldReturn400WhenStartDateIsInvalid() throws Exception {
        var payload = """
                {
                  "propertyId": "11111111-1111-1111-1111-111111111111",
                  "guestId": "11111111-1111-1111-1111-111111111111",
                  "startDate": "invalid-date",
                  "endDate": "2024-12-25",
                  "guestCount": 2
                }
                """;

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Data de início ou fim inválida!"));
    }

    @Test
    public void shouldReturn400WhenEndDateIsInvalid() throws Exception {
        var payload = """
                {
                  "propertyId": "11111111-1111-1111-1111-111111111111",
                  "guestId": "11111111-1111-1111-1111-111111111111",
                  "startDate": "2024-12-20",
                  "endDate": "invalid-date",
                  "guestCount": 2
                }
                """;

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Data de início ou fim inválida!"));
    }

    @Test
    public void shouldReturn422WhenGuestCountIsInvalid() throws Exception {
        var payload = """
                {
                  "propertyId": "11111111-1111-1111-1111-111111111111",
                  "guestId": "11111111-1111-1111-1111-111111111111",
                  "startDate": "2024-12-20",
                  "endDate": "2024-12-25",
                  "guestCount": 0
                }
                """;

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail")
                        .value("O número de hóspedes deve ser maior que zero!"));
    }

    @Test
    public void ShouldReturn400WhenPropertyIsInvalid() throws Exception {
        var payload = """
                {
                  "propertyId": "invalid-id",
                  "guestId": "11111111-1111-1111-1111-111111111111",
                  "startDate": "2024-12-20",
                  "endDate": "2024-12-25",
                  "guestCount": 2
                }
                """;

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("O corpo da requisição é inválido!"));
    }

    @Test
    public void shouldReturn404WhenPropertyIdIsInvalid() throws Exception {
        var payload = """
                {
                  "propertyId": "22222222-2222-2222-2222-222222222222",
                  "guestId": "11111111-1111-1111-1111-111111111111",
                  "startDate": "2024-12-20",
                  "endDate": "2024-12-25",
                  "guestCount": 2
                }
                """;

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Propriedade não encontrada!"));
    }

    @Test
    public void shouldReturn404WhenUserIdIsInvalid() throws Exception {
        var payload = """
                {
                  "propertyId": "11111111-1111-1111-1111-111111111111",
                  "guestId": "22222222-2222-2222-2222-222222222222",
                  "startDate": "2024-12-20",
                  "endDate": "2024-12-25",
                  "guestCount": 2
                }
                """;

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Usuário não encontrado!"));
    }

    @Test
    public void shouldCancelAReservation() throws Exception {
        var createPayload = """
                {
                  "propertyId": "11111111-1111-1111-1111-111111111111",
                  "guestId": "11111111-1111-1111-1111-111111111111",
                  "startDate": "2024-12-20",
                  "endDate": "2024-12-25",
                  "guestCount": 2
                }
                """;

        var result = mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isCreated())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        JsonNode json = objectMapper.readTree(body);
        String bookingId = json.get("booking").get("id").asText();

        mockMvc.perform(post("/bookings/{id}/cancel", bookingId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnAnErrorWhenCancelingAnInexistentReservation() throws Exception {
        mockMvc.perform(post("/bookings/{id}/cancel", "22222222-2222-2222-2222-222222222222"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Reserva não encontrada!"));
    }
}
