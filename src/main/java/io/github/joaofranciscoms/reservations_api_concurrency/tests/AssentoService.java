package io.github.joaofranciscoms.reservations_api_concurrency.tests;

import io.github.joaofranciscoms.reservations_api_concurrency.model.Assento;
import io.github.joaofranciscoms.reservations_api_concurrency.model.StatusAssento;
import io.github.joaofranciscoms.reservations_api_concurrency.repository.AssentoRepository;
import io.github.joaofranciscoms.reservations_api_concurrency.validator.AssentoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssentoService {

    private final AssentoRepository repository;
    private final AssentoValidator validator;

    public void salvar(Assento assento){
        assento.setStatus(StatusAssento.DISPONIVEL);
        repository.save(assento);
    }

    public Optional<Assento> obterDetalhes(UUID id){
        return repository.findById(id);
    }

    public void deletar(Assento assento){
        validator.verificarExistenciaReserva(assento);
        repository.delete(assento);
    }
}
