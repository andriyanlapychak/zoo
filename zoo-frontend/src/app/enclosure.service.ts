import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Enclosure } from './enclosure.model';

/** Holt die Gehege vom Backend (nur lesend, wie im Backend). */
@Injectable({ providedIn: 'root' })
export class EnclosureService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/api/enclosures';

  /** GET /api/enclosures: alle Gehege, vom Backend nach Namen sortiert. */
  getAll(): Observable<Enclosure[]> {
    return this.http.get<Enclosure[]>(this.baseUrl);
  }
}