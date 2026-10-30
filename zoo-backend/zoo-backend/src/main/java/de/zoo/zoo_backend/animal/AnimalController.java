package de.zoo.zoo_backend.animal;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * REST-Schnittstelle für Tiere unter der Adresse {@code /api/animals}.
 *
 * <p>Der Controller ist die Annahmestelle: Er kennt HTTP (Adressen, Methoden, Statuscodes),
 * enthält aber keine Fachlogik. Alles Fachliche delegiert er an den {@link AnimalService}.
 * Ein Request läuft so: Controller, dann Service, dann Repository, dann Datenbank.
 */
@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    /** {@code GET /api/animals}: liefert alle Tiere als JSON-Liste. Erfolg: Status 200. */
    @GetMapping
    public List<AnimalResponse> findAll() {
        return animalService.findAll();
    }

    /**
     * {@code GET /api/animals/{id}}: liefert ein einzelnes Tier. Erfolg: Status 200.
     * Gibt es die ID nicht: Status 404. Die ID wird aus der Adresse gelesen
     * ({@code @PathVariable}), zum Beispiel {@code /api/animals/2}.
     */
    @GetMapping("/{id}")
    public AnimalResponse findById(@PathVariable Long id) {
        return animalService.findById(id);
    }

    /**
     * {@code POST /api/animals}: legt ein neues Tier an. Der JSON-Body wird in ein
     * {@link AnimalRequest} umgewandelt.
     *
     * <p>{@code @Valid} löst die Prüfung der Formregeln aus dem Request-DTO aus. Ungültige
     * Eingabe (zum Beispiel leerer Name) führt zu Status 400, bevor der Service läuft.
     *
     * <p>Erfolg: Status 201 (Created) mit dem neuen Tier. Unbekannte Art oder unbekanntes
     * Gehege (Fachregel im Service): Status 400.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalResponse create(@Valid @RequestBody AnimalRequest request) {
        return animalService.create(request);
    }

     /**
     * {@code PUT /api/animals/{id}}: ändert ein bestehendes Tier vollständig. Der Body hat
     * dieselbe Form wie beim Anlegen ({@link AnimalRequest}) und wird mit {@code @Valid} geprüft.
     *
     * <p>Erfolg: Status 200 mit dem geänderten Tier. Gibt es die ID nicht: Status 404.
     * Ungültige Eingabe oder unbekannte Art/Gehege: Status 400.
     */
    @PutMapping("/{id}")
    public AnimalResponse update(@PathVariable Long id, @Valid @RequestBody AnimalRequest request) {
        return animalService.update(id, request);
    }

    /**
     * {@code DELETE /api/animals/{id}}: löscht ein Tier. Erfolg: Status 204 (No Content),
     * das heißt: erledigt, es gibt nichts zurückzugeben. Gibt es die ID nicht: Status 404.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        animalService.delete(id);
    }
}