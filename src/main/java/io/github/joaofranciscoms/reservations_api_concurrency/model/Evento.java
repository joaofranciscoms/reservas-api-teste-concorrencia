package io.github.joaofranciscoms.reservations_api_concurrency.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "evento")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @CreatedDate
    @Column(name = "data_evento")
    private LocalDateTime dataEvento;

    @Column(name = "local", length = 150, nullable = false)
    private String local;

    @OneToMany(mappedBy = "evento")
    @ToString.Exclude
    private List<Assento> assento;
}
