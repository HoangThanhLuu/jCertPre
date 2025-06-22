import {ApplicationConfig, importProvidersFrom, provideZoneChangeDetection} from '@angular/core';
import {provideRouter, withHashLocation} from '@angular/router';

import {routes} from './app.routes';
import {provideClientHydration, withNoHttpTransferCache} from '@angular/platform-browser';
import {HTTP_INTERCEPTORS, provideHttpClient, withFetch, withInterceptorsFromDi,} from '@angular/common/http';
import {provideAnimations} from '@angular/platform-browser/animations';
import {ModalModule} from 'ngx-bootstrap/modal';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {JwtInterceptor} from './shared/helper/jwt-interceptor';
import {ErrorInterceptor} from './shared/helper/error-interceptor';
import {provideToastr} from 'ngx-toastr';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideAnimationsAsync(),
    provideRouter(routes, withHashLocation()),
    provideClientHydration(withNoHttpTransferCache()),
    provideHttpClient(withFetch(), withInterceptorsFromDi()),
    provideAnimations(),
    importProvidersFrom(ModalModule.forRoot()),
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},
    provideToastr({
      preventDuplicates: true,
      countDuplicates: true,
    }),
  ]
};
