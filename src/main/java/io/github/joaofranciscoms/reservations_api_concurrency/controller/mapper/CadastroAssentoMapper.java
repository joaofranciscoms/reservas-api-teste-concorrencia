package io.github.joaofranciscoms.reservations_api_concurrency.controller.mapper;

import io.github.joaofranciscoms.reservations_api_concurrency.controller.dto.CadastroAssentoDTO;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Assento;
import io.github.joaofranciscoms.reservations_api_concurrency.repository.EventoRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CadastroAssentoMapper {

    @Autowired
    EventoRepository repository;

    @Mapping(expression = "java( repository.findById(cadastroAssentoDTO.idEvento()).orElse(null) )", target = "evento")
    public abstract Assento toEntity(CadastroAssentoDTO cadastroAssentoDTO);

    public  abstract CadastroAssentoDTO toDTO(Assento assento);
}
