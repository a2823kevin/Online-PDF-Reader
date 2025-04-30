import { ApplicationConfig, provideZoneChangeDetection, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { icons } from './icons-provider';
import { provideNzIcons } from 'ng-zorro-antd/icon';
import { zh_TW, provideNzI18n } from 'ng-zorro-antd/i18n';
import { registerLocaleData } from '@angular/common';
import zh from '@angular/common/locales/zh';
import { FormsModule } from '@angular/forms';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { JwtModule } from "@auth0/angular-jwt";

registerLocaleData(zh);

export function tokenGetter() {
  return localStorage.getItem("opr_token");
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes), 
    provideNzIcons(icons), 
    provideNzI18n(zh_TW), 
    importProvidersFrom(FormsModule), 
    provideAnimationsAsync(), 
    importProvidersFrom(
      JwtModule.forRoot({
          config: {
              tokenGetter: tokenGetter,
              // allowedDomains: ["example.com"],
              // disallowedRoutes: ["http://example.com/examplebadroute/"],
          },
      }),
  ),
    provideHttpClient(withInterceptorsFromDi())
  ]
};
