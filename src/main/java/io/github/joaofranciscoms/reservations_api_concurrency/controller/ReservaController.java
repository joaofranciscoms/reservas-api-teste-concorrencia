package io.github.joaofranciscoms.reservations_api_concurrency.controller;

import io.github.joaofranciscoms.reservations_api_concurrency.controller.dto.CadastroReservaDTO;
import io.github.joaofranciscoms.reservations_api_concurrency.controller.dto.RespostaReservaDTO;
import io.github.joaofranciscoms.reservations_api_concurrency.controller.mapper.CadastroReservaMapper;
import io.github.joaofranciscoms.reservations_api_concurrency.controller.mapper.RespostaReservaMapper;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Reserva;
import io.github.joaofranciscoms.reservations_api_concurrency.tests.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("reservas")
@RequiredArgsConstructor
public class ReservaController implements GenericController {

    private final ReservaService service;
    private final CadastroReservaMapper cadastroReservaMapper;
    private final RespostaReservaMapper respostaReservaMapper;

    @PostMapping("sem-lock")
    public ResponseEntity<Void> salvarReservaSemLock(@RequestBody CadastroReservaDTO reservaDTO) {
        Reserva reserva = cadastroReservaMapper.toEntity(reservaDTO);
        service.salvarSemLock(reserva);
        URI location = getURI(reserva.getId());
        return ResponseEntity.created(location).build();
    }

    @PostMapping("lock-otimista")
    public ResponseEntity<Void> salvarReservaLockOtimista(@RequestBody CadastroReservaDTO reservaDTO) {
        Reserva reserva = cadastroReservaMapper.toEntity(reservaDTO);
        service.salvarComLockOtimista(reserva);
        URI location = getURI(reserva.getId());
        return ResponseEntity.created(location).build();
    }

    @PostMapping("lock-pessimista")
    public ResponseEntity<Void> salvarReservaLockPessimista(@RequestBody CadastroReservaDTO reservaDTO) {
        Reserva reserva = cadastroReservaMapper.toEntity(reservaDTO);
        service.salvarComLockPessimista(reserva);
        URI location = getURI(reserva.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> obterReserva(@PathVariable("id") String id){
        var idReserva = UUID.fromString(id);
        Optional<Reserva> reservaOptional = service.obterDetalhes(idReserva);

        if(reservaOptional.isPresent()){
            Reserva reserva = reservaOptional.get();
            RespostaReservaDTO respostaReservaDTO = respostaReservaMapper.toDTO(reserva);
            return ResponseEntity.ok(respostaReservaDTO);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarReserva(@PathVariable("id") String id){
        var idReserva = UUID.fromString(id);
        Optional<Reserva> reservaOptional = service.obterDetalhes(idReserva);

        if(reservaOptional.isPresent()){
            Reserva reserva = reservaOptional.get();
            service.deletar(reserva);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
