package com.thiagosalvess.booking.infrastructure.web;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.thiagosalvess.booking.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class ApiExceptionHandler {
    private ProblemDetail problem(HttpStatus status, String type, String title, String detail, HttpServletRequest req) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setType(URI.create(type));
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setInstance(URI.create(req.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now().toString());
        return problemDetail;
    }

    // 400 – payload mal formatado / datas inválidas na desserialização
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        if (ex.getMostSpecificCause() instanceof DateTimeParseException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    problem(HttpStatus.BAD_REQUEST, "about:blank",
                            "Formato de data inválido", "Data de início ou fim inválida!", req)
            );
        }
        return ResponseEntity.badRequest().body(
                problem(HttpStatus.BAD_REQUEST, "about:blank",
                        "Requisição mal formatada", "O corpo da requisição é inválido!", req)
        );
    }

    // 422 – validações semânticas de domínio
    @ExceptionHandler({
            InvalidGuestCountException.class,
            GuestCountExceededException.class,
            InvalidDateRangeException.class
    })
    public ResponseEntity<ProblemDetail> handleUnprocessable(RuntimeException ex, HttpServletRequest req) {
        return ResponseEntity.unprocessableEntity().body(
                problem(HttpStatus.UNPROCESSABLE_ENTITY, "about:blank",
                        "Erro de validação", ex.getMessage(), req)
        );
    }

    // 409 – conflitos de negócio
    @ExceptionHandler({
            PropertyUnavailableException.class,
            BookingAlreadyCanceledException.class
    })
    public ResponseEntity<ProblemDetail> handleConflict(RuntimeException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                problem(HttpStatus.CONFLICT, "about:blank",
                        "Conflito de estado", ex.getMessage(), req)
        );
    }

    // 404 – recursos não encontrados
    @ExceptionHandler({
            BookingNotFoundException.class,
            PropertyNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ProblemDetail> handleNotFound(RuntimeException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                problem(HttpStatus.NOT_FOUND, "about:blank",
                        "Recurso não encontrado", ex.getMessage(), req)
        );
    }

    // 400 – campos obrigatórios ausentes em entidades
    @ExceptionHandler({
            PropertyNameRequiredException.class,
            MaxGuestsMustBePositiveException.class,
            UserIdRequiredException.class,
            UserNameRequiredException.class,
            PropertyBasePricePerNightRequiredException.class
    })
    public ResponseEntity<ProblemDetail> handleBadRequest(RuntimeException ex, HttpServletRequest req) {
        return ResponseEntity.badRequest().body(
                problem(HttpStatus.BAD_REQUEST, "about:blank",
                        "Parâmetros inválidos", ex.getMessage(), req)
        );
    }

    // 500 – fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                problem(HttpStatus.INTERNAL_SERVER_ERROR, "about:blank",
                        "Erro interno", "Ocorreu um erro inesperado.", req)
        );
    }
}
