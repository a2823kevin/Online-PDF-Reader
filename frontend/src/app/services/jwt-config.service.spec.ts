import { TestBed } from '@angular/core/testing';

import { JwtConfigService } from './jwt-config.service';

describe('JwtConfigService', () => {
  let service: JwtConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JwtConfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
