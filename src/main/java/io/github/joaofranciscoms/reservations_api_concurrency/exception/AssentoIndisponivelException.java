package io.github.joaofranciscoms.reservations_api_concurrency.exception;

public class AssentoIndisponivelException extends RuntimeException {
    public AssentoIndisponivelException(String message) {
        super(message);
    }
}
