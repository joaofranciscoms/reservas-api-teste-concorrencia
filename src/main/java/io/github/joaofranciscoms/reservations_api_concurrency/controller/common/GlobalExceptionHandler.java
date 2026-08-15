package io.github.joaofranciscoms.reservations_api_concurrency.controller.common;

import io.github.joaofranciscoms.reservations_api_concurrency.controller.dto.ErroRespostaDTO;
import io.github.joaofranciscoms.reservations_api_concurrency.exception.AssentoCadastradoEmEventoException;
import io.github.joaofranciscoms.reservations_api_concurrency.exception.AssentoIndisponivelException;
import io.github.joaofranciscoms.reservations_api_concurrency.exception.AssentoReservadoException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AssentoIndisponivelException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroRespostaDTO handlerAssentoIndisponivelException(AssentoIndisponivelException e){
        return new ErroRespostaDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(AssentoReservadoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroRespostaDTO handlerAssentoReservadoException(AssentoReservadoException e){
        return new ErroRespostaDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(AssentoCadastradoEmEventoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroRespostaDTO handlerAssentoCadastradoEmEventoException(AssentoCadastradoEmEventoException e){
        return new ErroRespostaDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroRespostaDTO handlerObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException e){
        return new ErroRespostaDTO(HttpStatus.CONFLICT.value(), e.getMessage(), List.of());
    }
}
