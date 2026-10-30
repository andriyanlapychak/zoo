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