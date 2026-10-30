import { Component, OnInit, inject, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { AnimalService } from './animal.service';
import { SpeciesService } from './species.service';
import { EnclosureService } from './enclosure.service';
import { AnimalRequest } from './animal.model';
import { Species } from './species.model';
import { Enclosure } from './enclosure.model';

/**
 * Formular zum Anlegen eines Tieres.
 * Sendet die Eingabe an das Backend und zeigt dessen Fehlermeldungen an.
 */
@Component({
  selector: 'app-animal-form',
  imports: [FormsModule], // FormsModule schaltet [(ngModel)] frei (Eingabefeld <-> Variable)
  template: `
    <h2>Neues Tier</h2>

    @if (error()) {
      <p style="color: red">{{ error() }}</p>
    }

    <!-- (ngSubmit): wird ausgelöst, wenn man Enter drückt oder auf "Speichern" klickt -->
    <form (ngSubmit)="save()">
      <p>
        <label>Name: <input name="name" [(ngModel)]="name" /></label>
      </p>
      <p>
        <label>Geburtsdatum: <input name="dob" type="date" [(ngModel)]="dateOfBirth" /></label>
      </p>
      <p>
        <label>Art:
          <select name="species" [(ngModel)]="speciesId">
            <option [ngValue]="null">– bitte wählen –</option>
            @for (s of species(); track s.id) {
              <!-- Angezeigt wird der Name, gespeichert wird die ID -->
              <option [ngValue]="s.id">{{ s.name }}</option>
            }
          </select>
        </label>
      </p>
      <p>
        <label>Gehege:
          <select name="enclosure" [(ngModel)]="enclosureId">
            <option [ngValue]="null">– keins –</option>
            @for (e of enclosures(); track e.id) {
              <option [ngValue]="e.id">{{ e.name }}</option>
            }
          </select>
        </label>
      </p>
      <button type="submit">Speichern</button>
    </form>
  `,
})
export class AnimalForm implements OnInit {

  private readonly animalService = inject(AnimalService);
  private readonly speciesService = inject(SpeciesService);
  private readonly enclosureService = inject(EnclosureService);

  // Inhalte der Dropdowns und Fehlertext. Signals, weil sie erst nach der Antwort des Backends gesetzt werden.
  readonly species = signal<Species[]>([]);
  readonly enclosures = signal<Enclosure[]>([]);
  readonly error = signal<string | null>(null);

  /** Meldung an die Elternseite: "Es wurde ein Tier gespeichert", damit sie die Liste neu lädt. */
  readonly created = output<void>();

  // Eingabefelder (mit den Feldern im Template verbunden)
  name = '';
  dateOfBirth = '';
  speciesId: number | null = null;
  enclosureId: number | null = null;

  /** Lädt beim Erscheinen die Auswahllisten. */
  ngOnInit(): void {
    this.speciesService.getAll().subscribe((list) => this.species.set(list));
    this.enclosureService.getAll().subscribe((list) => this.enclosures.set(list));
  }

  /** Baut aus der Eingabe den Request und schickt ihn ans Backend. */
  save(): void {
    this.error.set(null);

    const request: AnimalRequest = {
      name: this.name,
      dateOfBirth: this.dateOfBirth || null, // leeres Datumsfeld wird zu null
      speciesId: this.speciesId as number,   // Pflicht: das Backend lehnt null mit 400 ab
      enclosureId: this.enclosureId,
    };

    this.animalService.create(request).subscribe({
      next: () => {
        // Erfolg (201): Formular leeren, Elternseite informieren
        this.name = '';
        this.dateOfBirth = '';
        this.speciesId = null;
        this.enclosureId = null;
        this.created.emit();
      },
      // Fehler (z. B. 400): Meldung des Backends anzeigen
      error: (err: HttpErrorResponse) => this.error.set(this.toMessage(err)),
    });
  }

    /**
   * Holt die Fehlertexte aus der Backend-Antwort.
   * Das Backend liefert immer {status, messages}, siehe ApiError im Backend.
   */
  private toMessage(err: HttpErrorResponse): string {
    if (err.status === 0) {
      return 'Backend nicht erreichbar.';
    }
    const messages: string[] | undefined = err.error?.messages;
    return messages?.length ? messages.join(' | ') : 'Unbekannter Fehler.';
  }
}