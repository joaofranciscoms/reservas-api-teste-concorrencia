package io.github.joaofranciscoms.reservations_api_concurrency.controller.dto;

import java.util.UUID;

public record CadastroReservaDTO(String nomeCliente, UUID idAssento) {
}
