package io.github.joaofranciscoms.reservations_api_concurrency.repository;

import io.github.joaofranciscoms.reservations_api_concurrency.model.Assento;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservaRepository extends JpaRepository<Reserva, UUID> {
    boolean existsByAssento(Assento assento);
}
