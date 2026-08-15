package io.github.joaofranciscoms.reservations_api_concurrency.tests;

import io.github.joaofranciscoms.reservations_api_concurrency.model.Evento;
import io.github.joaofranciscoms.reservations_api_concurrency.repository.EventoRepository;
import io.github.joaofranciscoms.reservations_api_concurrency.validator.EventoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository repository;
    private final EventoValidator validator;

    public void salvar(Evento evento){
        repository.save(evento);
    }

    public Optional<Evento> obterDetalhes(UUID id){
        return repository.findById(id);
    }

    public void deletar(Evento evento){
        validator.verificarAssentoCadastradoEmEvento(evento);
        repository.delete(evento);
    }
}
