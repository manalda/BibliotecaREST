import { TestBed } from '@angular/core/testing';

import { LibrosServiceService } from './libros-service.service';

describe('LibrosServiceService', () => {
  let service: LibrosServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LibrosServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
