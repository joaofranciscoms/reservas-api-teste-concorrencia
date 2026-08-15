package io.github.joaofranciscoms.reservations_api_concurrency.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reserva")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nomeCliente", length = 50, nullable = false)
    private String nomeCliente;

    @Column(name = "data_reserva")
    @CreatedDate
    private LocalDateTime dataReserva;

    @Column(name = "status", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusReserva status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_assento")
    @ToString.Exclude
    private Assento assento;
}
