package io.github.joaofranciscoms.reservations_api_concurrency.controller.dto;

import java.util.List;

public record ErroRespostaDTO(int status, String mensagem, List<ErroCampoDTO> erros) {
}
