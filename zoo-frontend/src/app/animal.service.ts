import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Animal, AnimalRequest} from './animal.model';

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


  /**
   * Legt ein neues Tier an (POST /api/animals).
   * Das Backend antwortet mit 201 und dem gespeicherten Tier inklusive vergebener ID.
   */
  create(request: AnimalRequest): Observable<Animal> {
    return this.http.post<Animal>(this.baseUrl, request);
  }

    /**
   * Löscht ein Tier (DELETE /api/animals/{id}).
   * Das Backend antwortet bei Erfolg mit 204, also ohne Inhalt, daher Observable<void>.
   * Gibt es das Tier nicht, antwortet es mit 404 und einem Fehlertext.
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  
}