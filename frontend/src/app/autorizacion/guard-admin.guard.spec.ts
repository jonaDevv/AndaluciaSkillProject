import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { guardAdminGuard } from './guard-admin.guard';
import { Injectable } from '@angular/core';

describe('guardAdminGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => guardAdminGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
