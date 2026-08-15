package io.github.joaofranciscoms.reservations_api_concurrency.controller.dto;

import java.util.UUID;

public record CadastroAssentoDTO(String codigo, UUID idEvento) {
}
