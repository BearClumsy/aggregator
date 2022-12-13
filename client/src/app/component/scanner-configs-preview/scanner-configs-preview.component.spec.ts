import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScannerConfigsPreviewComponent } from './scanner-configs-preview.component';

describe('ScannerConfigsPreviewComponent', () => {
  let component: ScannerConfigsPreviewComponent;
  let fixture: ComponentFixture<ScannerConfigsPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScannerConfigsPreviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScannerConfigsPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
