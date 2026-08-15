package io.github.joaofranciscoms.reservations_api_concurrency.repository;

import io.github.joaofranciscoms.reservations_api_concurrency.model.Assento;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Evento;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Reserva;
import io.github.joaofranciscoms.reservations_api_concurrency.model.StatusAssento;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AssentoRepository extends JpaRepository<Assento, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")
    })
    @Query("SELECT a FROM Assento a WHERE a.id = :id")
    Optional<Assento> buscarComLockPessimista(@Param("id") UUID id);

    @Modifying
    @Query("UPDATE Assento a SET a.status = :status WHERE a.id = :id")
    void atualizarStatusSemLock(@Param("id") UUID id, @Param("status") StatusAssento status);

    boolean existsByEvento(Evento evento);
}
