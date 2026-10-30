package de.zoo.zoo_backend.animal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Request-DTO: die Daten, die ein Client zum Anlegen eines Tieres schickt
 * ({@code POST /api/animals}).
 *
 * <p>Das ist der Vertrag nach außen für die Eingabe. Bewusst enthält er keine ID, denn die
 * vergibt die Datenbank. Art und Gehege werden nur über ihre IDs angegeben (wie ein
 * Fremdschlüssel). Der Service löst die IDs auf und prüft dabei, ob es sie gibt.
 *
 * <p>Die Annotationen an den Feldern sind Formregeln. Sie werden geprüft, sobald der
 * Controller den Parameter mit {@code @Valid} annimmt. Verletzt die Eingabe eine Regel,
 * antwortet Spring automatisch mit Status 400, ohne dass der Service überhaupt aufgerufen wird.
 *
 * @param name        Name des Tieres, Pflicht, nicht leer, höchstens 100 Zeichen
 * @param dateOfBirth Geburtsdatum, optional, darf nicht in der Zukunft liegen
 * @param speciesId   ID der Art aus {@code GET /api/species}, Pflicht
 * @param enclosureId ID des Geheges aus {@code GET /api/enclosures}, {@code null} heißt kein Gehege
 */
public record AnimalRequest(

        // NotBlank verbietet null, leeren Text und Text nur aus Leerzeichen.
        // Die Datenbank (NOT NULL) würde einen leeren Text "" dagegen akzeptieren.
        @NotBlank(message = "Name darf nicht leer sein")
        @Size(max = 100, message = "Name darf höchstens 100 Zeichen lang sein")
        String name,

        // Fehlt das Datum, ist das gültig (optional). Ein vorhandenes darf nicht in der Zukunft liegen.
        @PastOrPresent(message = "Geburtsdatum darf nicht in der Zukunft liegen")
        LocalDate dateOfBirth,

        // Ohne speciesId könnte der Service die Art gar nicht suchen. Darum Pflicht.
        @NotNull(message = "speciesId ist Pflicht")
        Long speciesId,

        Long enclosureId) {
}