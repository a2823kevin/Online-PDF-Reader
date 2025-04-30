import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class JwtConfigService {
  domain: string = '';

  load(): void {
    this.domain = window.location.hostname;
  }

  getAllowedDomains(): string[] {
    return [this.domain];
  }
  constructor() { }
}
