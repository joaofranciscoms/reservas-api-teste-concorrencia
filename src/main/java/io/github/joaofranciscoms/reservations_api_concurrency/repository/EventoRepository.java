package io.github.joaofranciscoms.reservations_api_concurrency.repository;

import io.github.joaofranciscoms.reservations_api_concurrency.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface EventoRepository extends JpaRepository<Evento, UUID>, JpaSpecificationExecutor<Evento> {
}
