package io.github.joaofranciscoms.reservations_api_concurrency.controller;

import io.github.joaofranciscoms.reservations_api_concurrency.controller.dto.CadastroEventoDTO;
import io.github.joaofranciscoms.reservations_api_concurrency.controller.dto.RespostaEventoDTO;
import io.github.joaofranciscoms.reservations_api_concurrency.controller.mapper.CadastroEventoMapper;
import io.github.joaofranciscoms.reservations_api_concurrency.controller.mapper.RespostaEventoMapper;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Evento;
import io.github.joaofranciscoms.reservations_api_concurrency.tests.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("eventos")
@RequiredArgsConstructor
public class EventoController implements GenericController {

    private final EventoService service;
    private final CadastroEventoMapper cadastroEventoMapper;
    private final RespostaEventoMapper respostaEventoMapper;

    @PostMapping
    public ResponseEntity<Void> salvarEvento(@RequestBody CadastroEventoDTO eventoDTO){
        Evento evento = cadastroEventoMapper.toEntity(eventoDTO);
        service.salvar(evento);
        URI location = getURI(evento.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> obterEvento(@PathVariable("id") String id){
        var idEvento = UUID.fromString(id);
        Optional<Evento> eventoOptional = service.obterDetalhes(idEvento);

        if(eventoOptional.isPresent()){
            Evento evento = eventoOptional.get();
            RespostaEventoDTO respostaEventoDTO = respostaEventoMapper.toDTO(evento);
            return ResponseEntity.ok(respostaEventoDTO);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarEvento(@PathVariable("id") String id){
        var idEvento = UUID.fromString(id);
        Optional<Evento> eventoOptional = service.obterDetalhes(idEvento);

        if(eventoOptional.isPresent()){
            Evento evento = eventoOptional.get();
            service.deletar(evento);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
