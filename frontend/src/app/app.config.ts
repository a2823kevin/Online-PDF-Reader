import { ApplicationConfig, provideZoneChangeDetection, importProvidersFrom, APP_INITIALIZER } from '@angular/core';
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
import { JWT_OPTIONS, JwtModule } from "@auth0/angular-jwt";
import { JwtConfigService } from './services/jwt-config.service';

registerLocaleData(zh);

export function tokenGetter() {
  return localStorage.getItem("opr_token");
}

export function provideJwtOptions(jwtConfigService: JwtConfigService) {
  jwtConfigService.load();
  console.log(jwtConfigService.getAllowedDomains());
  return {
    tokenGetter: tokenGetter,
    allowedDomains: jwtConfigService.getAllowedDomains(),
  };
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
              allowedDomains: []
          },
      }),
    ),
    {
      provide: JWT_OPTIONS,
      useFactory: provideJwtOptions,
      deps: [JwtConfigService],
    },
    provideHttpClient(withInterceptorsFromDi())
  ]
};
