import { Component, OnInit, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { AnimalService } from './animal.service';
import { Animal } from './animal.model';

/**
 * Zeigt die Tierliste als Tabelle an und erlaubt das Löschen einzelner Tiere.
 * Sie fragt den AnimalService nach den Daten und kümmert sich nur um die Anzeige.
 */
@Component({
  selector: 'app-animal-list', // So wird die Komponente in einer anderen Seite eingebaut
  template: `
    <h2>Tiere</h2>

    <!-- Fehlermeldung, nur sichtbar wenn error() einen Text enthält -->
    @if (error()) {
      <p style="color: red">{{ error() }}</p>
    }

    <table border="1" cellpadding="6">
      <thead>
        <tr>
          <th>ID</th><th>Name</th><th>Art</th><th>Geburtsdatum</th><th>Gehege</th>
          <th></th> <!-- NEU: Spalte für den Löschen-Button -->
        </tr>
      </thead>
      <tbody>
        <!-- Eine Zeile pro Tier; track = woran Angular ein Tier wiedererkennt -->
        @for (animal of animals(); track animal.id) {
          <tr>
            <td>{{ animal.id }}</td>
            <td>{{ animal.name }}</td>
            <td>{{ animal.speciesName }}</td>
            <!-- ?? '–' zeigt einen Strich, wenn der Wert fehlt (null) -->
            <td>{{ animal.dateOfBirth ?? '–' }}</td>
            <td>{{ animal.enclosureName ?? '–' }}</td>
            <td>
              <!-- NEU: (click) ruft remove() mit genau dem Tier dieser Zeile auf -->
              <button type="button" (click)="remove(animal)">Löschen</button>
            </td>
          </tr>
        }
      </tbody>
    </table>
  `,
})
export class AnimalList implements OnInit {

  // Der Service wird von Angular bereitgestellt (Dependency Injection)
  private readonly animalService = inject(AnimalService);

  // Signal = Wert, bei dessen Änderung Angular die Anzeige selbst aktualisiert
  readonly animals = signal<Animal[]>([]);
  readonly error = signal<string | null>(null);

  /** Wird von Angular einmal aufgerufen, sobald die Komponente erscheint. */
  ngOnInit(): void {
    this.load();
  }

  /** Lädt die Tierliste neu. Wird auch nach dem Speichern oder Löschen aufgerufen. */
  load(): void {
    this.error.set(null);
    this.animalService.getAll().subscribe({
      next: (list) => this.animals.set(list),
      error: () => this.error.set('Tiere konnten nicht geladen werden.'),
    });
  }

  /**
   * NEU: Löscht das Tier nach einer Rückfrage und lädt danach die Liste neu.
   * Die Rückfrage verhindert, dass ein versehentlicher Klick Daten vernichtet.
   */
  remove(animal: Animal): void {
    if (!window.confirm(`Tier "${animal.name}" wirklich löschen?`)) {
      return; // Abbruch: nichts passiert
    }
    this.animalService.delete(animal.id).subscribe({
      // Erfolg (204): aktuelle Liste vom Backend holen
      next: () => this.load(),
      // Fehler (z. B. 404, weil das Tier schon weg ist): Text des Backends anzeigen
      error: (err: HttpErrorResponse) => {
        const messages: string[] | undefined = err.error?.messages;
        this.error.set(messages?.length ? messages.join(' | ') : 'Löschen fehlgeschlagen.');
      },
    });
  }
}