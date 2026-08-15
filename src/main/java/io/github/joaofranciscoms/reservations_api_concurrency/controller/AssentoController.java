package io.github.joaofranciscoms.reservations_api_concurrency.controller;

import io.github.joaofranciscoms.reservations_api_concurrency.controller.dto.CadastroAssentoDTO;
import io.github.joaofranciscoms.reservations_api_concurrency.controller.dto.RespostaAssentoDTO;
import io.github.joaofranciscoms.reservations_api_concurrency.controller.mapper.CadastroAssentoMapper;
import io.github.joaofranciscoms.reservations_api_concurrency.controller.mapper.RespostaAssentoMapper;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Assento;
import io.github.joaofranciscoms.reservations_api_concurrency.tests.AssentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("assentos")
@RequiredArgsConstructor
public class AssentoController implements GenericController {

    private final AssentoService service;
    private final CadastroAssentoMapper cadastroAssentoMapper;
    private final RespostaAssentoMapper respostaAssentoMapper;

    @PostMapping
    public ResponseEntity<Void> salvarAssento(@RequestBody CadastroAssentoDTO assentoDTO){
        Assento assento = cadastroAssentoMapper.toEntity(assentoDTO);
        service.salvar(assento);
        URI location = getURI(assento.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> obterAssento(@PathVariable("id") String id){
        var idAssento = UUID.fromString(id);
        Optional<Assento> assentoOptional = service.obterDetalhes(idAssento);

        if(assentoOptional.isPresent()){
            Assento assento = assentoOptional.get();
            RespostaAssentoDTO respostaAssentoDTO = respostaAssentoMapper.toDTO(assento);
            return ResponseEntity.ok(respostaAssentoDTO);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarAssento(@PathVariable("id") String id){
        var idAssento = UUID.fromString(id);
        Optional<Assento> assentoOptional = service.obterDetalhes(idAssento);

        if(assentoOptional.isPresent()){
            Assento assento = assentoOptional.get();
            service.deletar(assento);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
