package io.github.joaofranciscoms.reservations_api_concurrency.controller.mapper;

import io.github.joaofranciscoms.reservations_api_concurrency.controller.dto.CadastroEventoDTO;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Evento;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CadastroEventoMapper {
    Evento toEntity(CadastroEventoDTO cadastroEventoDTO);
    CadastroEventoDTO toDTO(Evento evento);
}
