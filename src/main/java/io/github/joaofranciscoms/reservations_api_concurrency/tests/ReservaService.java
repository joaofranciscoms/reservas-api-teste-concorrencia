package io.github.joaofranciscoms.reservations_api_concurrency.tests;

import io.github.joaofranciscoms.reservations_api_concurrency.model.Assento;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Reserva;
import io.github.joaofranciscoms.reservations_api_concurrency.model.StatusAssento;
import io.github.joaofranciscoms.reservations_api_concurrency.model.StatusReserva;
import io.github.joaofranciscoms.reservations_api_concurrency.repository.AssentoRepository;
import io.github.joaofranciscoms.reservations_api_concurrency.repository.ReservaRepository;
import io.github.joaofranciscoms.reservations_api_concurrency.validator.ReservaValidator;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final AssentoRepository assentoRepository;
    private final ReservaValidator validator;

    @Transactional
    public void salvarSemLock(Reserva reserva) {
        Assento assento = assentoRepository.findById(reserva.getAssento().getId()).orElseThrow(() -> new EntityNotFoundException("Assento não encontrado"));

        validator.verificarReserva(reserva, assento);

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restaura o status de interrupção
            throw new RuntimeException("Thread interrompida durante o gap proposital", e);
        }

        assentoRepository.atualizarStatusSemLock(assento.getId(), StatusAssento.RESERVADO);

        reserva.setAssento(assento);
        reserva.setStatus(StatusReserva.CONFIRMADA);
        reservaRepository.save(reserva);
    }

    @Transactional
    public void salvarComLockOtimista(Reserva reserva){
        Assento assento = assentoRepository.findById(reserva.getAssento().getId()).orElseThrow(() -> new EntityNotFoundException("Assento não encontrado"));

        validator.verificarReserva(reserva, assento);

        assento.setStatus(StatusAssento.RESERVADO);

        reserva.setAssento(assento);
        reserva.setStatus(StatusReserva.CONFIRMADA);
        reservaRepository.save(reserva);
    }

    @Transactional
    public void salvarComLockPessimista(Reserva reserva){
        Assento assento = assentoRepository.buscarComLockPessimista(reserva.getAssento().getId()).orElseThrow(() -> new EntityNotFoundException("Assento não encontrado"));

        validator.verificarReserva(reserva, assento);

        assento.setStatus(StatusAssento.RESERVADO);

        reserva.setAssento(assento);
        reserva.setStatus(StatusReserva.CONFIRMADA);
        reservaRepository.save(reserva);
    }

    public Optional<Reserva> obterDetalhes(UUID id){
        return reservaRepository.findById(id);
    }

    public void deletar(Reserva reserva){
        Assento assento = assentoRepository.findById(reserva.getAssento().getId()).orElseThrow(() -> new EntityNotFoundException("Assento não encontrado"));
        assento.setStatus(StatusAssento.DISPONIVEL);
        reservaRepository.delete(reserva);
    }
}