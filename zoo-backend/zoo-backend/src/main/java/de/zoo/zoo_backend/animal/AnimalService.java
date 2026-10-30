package de.zoo.zoo_backend.animal;

import de.zoo.zoo_backend.enclosure.Enclosure;
import de.zoo.zoo_backend.enclosure.EnclosureRepository;
import de.zoo.zoo_backend.species.Species;
import de.zoo.zoo_backend.species.SpeciesRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Fachlogik für Tiere. Der Service liegt zwischen Controller und Repository: Hier gelten die
 * Geschäftsregeln, der Controller kennt nur HTTP und das Repository nur die Datenbank.
 *
 * <p>Die benötigten Repositories bekommt die Klasse von Spring im Konstruktor geliefert
 * (Dependency Injection). Sie baut sich ihre Helfer nicht selbst.
 */
@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final SpeciesRepository speciesRepository;
    private final EnclosureRepository enclosureRepository;

    public AnimalService(AnimalRepository animalRepository,
                         SpeciesRepository speciesRepository,
                         EnclosureRepository enclosureRepository) {
        this.animalRepository = animalRepository;
        this.speciesRepository = speciesRepository;
        this.enclosureRepository = enclosureRepository;
    }

    /**
     * Liefert alle Tiere. Das Umwandeln in das DTO passiert innerhalb der Transaktion,
     * solange die Datenbankverbindung offen ist. {@code readOnly} zeigt: Es wird nichts verändert.
     */
    @Transactional(readOnly = true)
    public List<AnimalResponse> findAll() {
        return animalRepository.findAll().stream()
                .map(AnimalResponse::from)
                .toList();
    }

    /**
     * Liefert ein einzelnes Tier.
     *
     * @param id ID des Tieres aus der Adresse
     * @return das gefundene Tier
     * @throws ResponseStatusException mit Status 404, wenn es kein Tier mit dieser ID gibt
     */
    @Transactional(readOnly = true)
    public AnimalResponse findById(Long id) {
        return AnimalResponse.from(getAnimalOrThrow(id));
    }

    /**
     * Legt ein neues Tier an.
     *
     * <p>Die Formregeln der Eingabe (Name nicht leer, Datum nicht in der Zukunft, speciesId
     * vorhanden) sind schon geprüft, bevor diese Methode läuft. Siehe {@link AnimalRequest}.
     * Hier gelten nur Fachregeln, die Daten aus der Datenbank brauchen:
     * <ul>
     *   <li>Die angegebene Art muss existieren, sonst Fehler 400.</li>
     *   <li>Ein Gehege ist optional. Wenn eines angegeben ist, muss es existieren, sonst 400.</li>
     * </ul>
     * Die ID vergibt die Datenbank. Die Methode läuft als eine Arbeitseinheit: Entweder wird
     * das Tier gespeichert, oder gar nichts.
     *
     * @param request die bereits formal geprüfte Eingabe des Clients
     * @return das gespeicherte Tier, jetzt mit seiner neuen ID
     * @throws ResponseStatusException mit Status 400, wenn Art oder Gehege nicht existieren
     */
    @Transactional
    public AnimalResponse create(AnimalRequest request) {
        Animal animal = new Animal();
        applyRequest(animal, request);

        // save() schreibt die Zeile (INSERT) und liefert das Objekt jetzt mit ID zurück.
        return AnimalResponse.from(animalRepository.save(animal));
    }

    /**
     * Ändert ein bestehendes Tier. Es gelten dieselben Regeln wie beim Anlegen.
     *
     * <p>PUT ersetzt das Tier vollständig: Alle Felder der Eingabe überschreiben die alten
     * Werte. Wird zum Beispiel {@code enclosureId} weggelassen, kommt das Tier aus seinem
     * Gehege heraus.
     *
     * <p>Es gibt hier bewusst kein {@code save()}. Das Tier wurde innerhalb dieser Transaktion
     * geladen, Hibernate überwacht es und schreibt jede Änderung beim Abschluss der
     * Transaktion selbst als UPDATE (Dirty Checking).
     *
     * @param id      ID des Tieres aus der Adresse
     * @param request die bereits formal geprüfte neue Eingabe
     * @return das geänderte Tier
     * @throws ResponseStatusException mit Status 404, wenn es das Tier nicht gibt,
     *                                 oder 400, wenn Art oder Gehege nicht existieren
     */
    @Transactional
    public AnimalResponse update(Long id, AnimalRequest request) {
        // Reihenfolge: erst das Tier (sonst 404), dann Art und Gehege (sonst 400).
        Animal animal = getAnimalOrThrow(id);
        applyRequest(animal, request);
        return AnimalResponse.from(animal);
    }

    /**
     * Löscht ein Tier.
     *
     * <p>Gibt es das Tier nicht, antwortet die Methode mit 404, statt stillschweigend Erfolg
     * zu melden. So merkt der Client, dass er eine falsche ID verwendet hat.
     *
     * @param id ID des Tieres aus der Adresse
     * @throws ResponseStatusException mit Status 404, wenn es kein Tier mit dieser ID gibt
     */
    @Transactional
    public void delete(Long id) {
        animalRepository.delete(getAnimalOrThrow(id));
    }

    /**
     * Hilfsmethode: überträgt die Eingabe auf ein Tier (neu oder bestehend). Sie steht an einer
     * Stelle, damit Anlegen und Ändern dieselben Fachregeln haben: Die Art muss existieren,
     * das Gehege ist optional, muss aber existieren, wenn es angegeben ist.
     */
    private void applyRequest(Animal animal, AnimalRequest request) {
        animal.setName(request.name());
        animal.setDateOfBirth(request.dateOfBirth());
        animal.setSpecies(getSpeciesOrThrow(request.speciesId()));
        animal.setEnclosure(getEnclosureOrNull(request.enclosureId()));
    }

    /** Hilfsmethode: sucht ein Tier nach ID oder wirft 404 (das Tier steht in der Adresse). */
    private Animal getAnimalOrThrow(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tier mit id " + id + " existiert nicht"));
    }

    /** Hilfsmethode: sucht eine Art nach ID oder wirft 400 (die Art steht im Body). */
    private Species getSpeciesOrThrow(Long speciesId) {
        return speciesRepository.findById(speciesId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Art mit id " + speciesId + " existiert nicht"));
    }

    /**
     * Hilfsmethode: sucht ein Gehege nach ID. Das Gehege ist optional, deshalb liefert die
     * Methode {@code null}, wenn keine ID angegeben ist. Ist eine ID angegeben, aber unbekannt,
     * wirft sie 400.
     */
    private Enclosure getEnclosureOrNull(Long enclosureId) {
        if (enclosureId == null) {
            return null;
        }
        return enclosureRepository.findById(enclosureId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Gehege mit id " + enclosureId + " existiert nicht"));
    }
}