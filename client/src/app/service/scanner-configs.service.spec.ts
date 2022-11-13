import { TestBed } from '@angular/core/testing';

import { ScannerConfigsService } from './scanner-configs.service';

describe('ScannerConfigsService', () => {
  let service: ScannerConfigsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScannerConfigsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
