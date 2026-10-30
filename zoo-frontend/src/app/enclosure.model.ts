/**
 * Ein Gehege, so wie das Backend es liefert (Gegenstück zu EnclosureResponse in Java).
 */
export interface Enclosure {
  id: number;
  name: string;
  sizeM2: number;
  description: string | null;
}