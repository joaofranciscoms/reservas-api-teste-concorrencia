package io.github.joaofranciscoms.reservations_api_concurrency.controller.dto;

import io.github.joaofranciscoms.reservations_api_concurrency.model.StatusAssento;

import java.util.UUID;

public record RespostaAssentoDTO(UUID id, String codigo, StatusAssento status, long version, UUID idEvento) {
}
