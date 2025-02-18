import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { guardExpertGuard } from './guard-expert.guard';

describe('guardExpertGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => guardExpertGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
