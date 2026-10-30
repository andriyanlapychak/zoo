/**
 * Eine Tierart, so wie das Backend sie liefert (Gegenstück zu SpeciesResponse in Java).
 * Das Formular braucht davon id und name für das Dropdown.
 */
export interface Species {
  id: number;
  name: string;
  category: string | null;
  food: string | null;
  dailyFoodKg: number;
}