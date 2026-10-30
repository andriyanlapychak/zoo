import { Component, signal } from '@angular/core';
import { AnimalList } from './animal-list';
import { AnimalForm } from './animal-form';

@Component({
  imports: [AnimalList, AnimalForm],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('zoo-frontend');
}
