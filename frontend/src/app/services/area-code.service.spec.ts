import { TestBed } from '@angular/core/testing';

import { AreaCodeService } from './area-code.service';

describe('AreaCodeService', () => {
  let service: AreaCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AreaCodeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
