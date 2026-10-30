package de.zoo.zoo_backend.error;

import java.util.List;

/**
 * Einheitliches Fehlerformat der API (Teil des API-Vertrags).
 * Das Frontend liest nur diese beiden Felder.
 *
 * @param status   HTTP-Statuscode, z. B. 400 oder 404
 * @param messages eine oder mehrere verständliche Fehlertexte für den Nutzer
 */
public record ApiError(int status, List<String> messages) { }