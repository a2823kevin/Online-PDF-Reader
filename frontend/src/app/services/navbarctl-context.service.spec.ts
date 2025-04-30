import { TestBed } from '@angular/core/testing';

import { NavbarctlContextService } from './navbarctl-context.service';

describe('NavbarctlContextService', () => {
  let service: NavbarctlContextService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NavbarctlContextService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
