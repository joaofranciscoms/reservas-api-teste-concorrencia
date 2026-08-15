package io.github.joaofranciscoms.reservations_api_concurrency.validator;

import io.github.joaofranciscoms.reservations_api_concurrency.exception.AssentoIndisponivelException;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Assento;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Reserva;
import io.github.joaofranciscoms.reservations_api_concurrency.model.StatusAssento;
import io.github.joaofranciscoms.reservations_api_concurrency.repository.AssentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservaValidator {

    private final AssentoRepository repository;

    public void verificarReserva(Reserva reserva, Assento assentoAtual){
        verificarDisponibilidadeAssento(assentoAtual);
    }

    private void verificarDisponibilidadeAssento(Assento assento){
        if(assento.getStatus() == StatusAssento.RESERVADO){
            throw new AssentoIndisponivelException("Este assento já está reservado!");
        }
    }
}
