/**
 * Ein Tier, so wie das Backend es liefert (Gegenstück zu AnimalResponse in Java).
 * Die Feldnamen müssen exakt zum JSON passen, sonst bleibt das Feld leer.
 * "| null" heißt: Der Wert darf fehlen (Geburtsdatum und Gehege sind optional).
 */
export interface Animal {
  id: number;
  name: string;
  dateOfBirth: string | null;   // JSON liefert Datum als Text, z. B. "2020-05-01"
  speciesName: string;
  enclosureName: string | null; // null = Tier ist keinem Gehege zugeordnet
}

/**
 * Das, was Angular beim Anlegen oder Ändern an das Backend schickt
 * (Gegenstück zu AnimalRequest in Java). Referenzen gehen als ID, nicht als Objekt.
 */
export interface AnimalRequest {
  name: string;
  dateOfBirth: string | null;  // Format "2020-05-01" oder null
  speciesId: number;           // Pflicht: das Backend lehnt sonst mit 400 ab
  enclosureId: number | null;  // optional: null = kein Gehege
}