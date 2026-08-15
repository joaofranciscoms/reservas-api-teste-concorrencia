package io.github.joaofranciscoms.reservations_api_concurrency.controller.mapper;

import io.github.joaofranciscoms.reservations_api_concurrency.controller.dto.RespostaReservaDTO;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Reserva;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RespostaReservaMapper {

    @Mapping(expression = "java( reserva.getAssento().getId() )", target = "idAssento")
    RespostaReservaDTO toDTO(Reserva reserva);
}
