package io.github.joaofranciscoms.reservations_api_concurrency.controller.mapper;

import io.github.joaofranciscoms.reservations_api_concurrency.controller.dto.CadastroReservaDTO;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Reserva;
import io.github.joaofranciscoms.reservations_api_concurrency.repository.AssentoRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CadastroReservaMapper {

    @Autowired
    AssentoRepository repository;

    @Mapping(expression = "java( repository.findById(cadastroReservaDTO.idAssento()).orElse(null) )", target = "assento")
    public abstract Reserva toEntity(CadastroReservaDTO cadastroReservaDTO);
}
