package io.github.joaofranciscoms.reservations_api_concurrency.controller.mapper;

import io.github.joaofranciscoms.reservations_api_concurrency.controller.dto.RespostaEventoDTO;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Evento;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RespostaEventoMapper {

    RespostaEventoDTO toDTO(Evento evento);
}
