package io.github.joaofranciscoms.reservations_api_concurrency.controller.dto;

import io.github.joaofranciscoms.reservations_api_concurrency.model.StatusReserva;

import java.time.LocalDateTime;
import java.util.UUID;

public record RespostaReservaDTO(UUID id, String nomeCliente, LocalDateTime dataReserva, StatusReserva status, UUID idAssento) {
}
