package de.zoo.zoo_backend.error;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * Zentrale Fehlerbehandlung: Übersetzt Ausnahmen in das einheitliche Fehlerformat ApiError.
 * Gilt für alle Controller, die Controller selbst bleiben frei von Fehlerbehandlung.
 */
@RestControllerAdvice // "Rat" für alle Controller: fängt deren Ausnahmen ab und antwortet als JSON
public class GlobalExceptionHandler {

    /**
     * Formfehler aus der Bean Validation (@Valid scheitert), z. B. Name leer.
     * Antwort: 400 mit einem Text pro verletzter Regel.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage) // der "message" aus @NotBlank usw. im DTO
                .toList();
        return ResponseEntity.badRequest().body(new ApiError(400, messages));
    }

    /**
     * Fachfehler, die der Service bewusst wirft (400 "Art existiert nicht", 404 "Tier existiert nicht").
     * Status und Text kommen unverändert aus der Ausnahme.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleStatus(ResponseStatusException ex) {
        String text = ex.getReason() != null ? ex.getReason() : "Fehler";
        int status = ex.getStatusCode().value();
        return ResponseEntity.status(status).body(new ApiError(status, List.of(text)));
    }
}