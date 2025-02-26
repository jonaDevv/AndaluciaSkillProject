import { TestBed } from '@angular/core/testing';

import { ParticipantesServiceService } from './participantes-service.service';

describe('ParticipantesServiceService', () => {
  let service: ParticipantesServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParticipantesServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
