package io.github.joaofranciscoms.reservations_api_concurrency.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RespostaEventoDTO(UUID id, String nome, LocalDateTime dataEvento, String local) {
}
