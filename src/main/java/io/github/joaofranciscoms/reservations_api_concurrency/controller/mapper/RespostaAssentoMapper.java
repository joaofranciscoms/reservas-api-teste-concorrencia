package io.github.joaofranciscoms.reservations_api_concurrency.controller.mapper;

import io.github.joaofranciscoms.reservations_api_concurrency.controller.dto.RespostaAssentoDTO;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Assento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RespostaAssentoMapper {

    @Mapping(expression = "java( assento.getEvento().getId() )", target = "idEvento")
    RespostaAssentoDTO toDTO(Assento assento);
}
