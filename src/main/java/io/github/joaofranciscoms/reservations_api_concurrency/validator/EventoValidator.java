package io.github.joaofranciscoms.reservations_api_concurrency.validator;

import io.github.joaofranciscoms.reservations_api_concurrency.exception.AssentoCadastradoEmEventoException;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Evento;
import io.github.joaofranciscoms.reservations_api_concurrency.repository.AssentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventoValidator {

    private final AssentoRepository repository;

    public void verificarAssentoCadastradoEmEvento(Evento evento){
        if(isAssentoCadastradoEmEvento(evento)){
            throw new AssentoCadastradoEmEventoException("Este evento possui assentos alocados!");
        }
    }

    private boolean isAssentoCadastradoEmEvento(Evento evento){
        return repository.existsByEvento(evento);
    }
}
