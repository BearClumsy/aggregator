import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScannerConfigsComponent } from './scanner-configs.component';

describe('ScannerConfigsComponent', () => {
  let component: ScannerConfigsComponent;
  let fixture: ComponentFixture<ScannerConfigsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScannerConfigsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScannerConfigsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
