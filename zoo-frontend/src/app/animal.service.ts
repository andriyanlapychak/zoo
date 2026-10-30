import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Animal } from './animal.model';

/**
 * Spricht mit der Tier-API des Backends.
 * Hier steht nur der HTTP-Zugriff, keine Anzeige und keine Fachregeln.
 */
@Injectable({ providedIn: 'root' }) // Angular erzeugt genau eine Instanz und reicht sie weiter
export class AnimalService {

  // Angular übergibt den HttpClient (Dependency Injection, wie der Konstruktor im Backend)
  private readonly http = inject(HttpClient);

  // Adresse des Backends. Später gehört sie in eine Konfiguration.
  private readonly baseUrl = 'http://localhost:8080/api/animals';

  /**
   * Holt alle Tiere (GET /api/animals).
   * Observable = Zusage auf eine spätere Antwort: Der Aufruf passiert erst,
   * wenn jemand die Antwort abonniert, und sie kommt asynchron an.
   */
  getAll(): Observable<Animal[]> {
    return this.http.get<Animal[]>(this.baseUrl);
  }
}