import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
// NEU: schaltet den HTTP-Client frei, mit dem Angular REST-APIs aufruft
import { provideHttpClient } from '@angular/common/http';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    // NEU: ohne diese Zeile kennt Angular keinen HttpClient,
    // und der Service unten würde beim Start mit einem Fehler abbrechen
    provideHttpClient()
  ]
};