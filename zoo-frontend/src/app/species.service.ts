import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Species } from './species.model';

/** Holt die Tierarten vom Backend (nur lesend, wie im Backend). */
@Injectable({ providedIn: 'root' })
export class SpeciesService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/api/species';

  /** GET /api/species: alle Arten, vom Backend nach Namen sortiert. */
  getAll(): Observable<Species[]> {
    return this.http.get<Species[]>(this.baseUrl);
  }
}