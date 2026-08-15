package io.github.joaofranciscoms.reservations_api_concurrency.validator;

import io.github.joaofranciscoms.reservations_api_concurrency.exception.AssentoReservadoException;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Assento;
import io.github.joaofranciscoms.reservations_api_concurrency.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssentoValidator {

    private final ReservaRepository repository;

    public void verificarExistenciaReserva(Assento assento){
        if(isAssentoReservado(assento)){
            throw new AssentoReservadoException("Não é possível exlcluir um assento reservado!");
        }
    }

    private boolean isAssentoReservado(Assento assento){
       return repository.existsByAssento(assento);
    }
}
