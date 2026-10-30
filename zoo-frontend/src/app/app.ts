import { Component, signal } from '@angular/core';
import { AnimalList } from './animal-list';

@Component({
  imports: [AnimalList],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('zoo-frontend');
}
