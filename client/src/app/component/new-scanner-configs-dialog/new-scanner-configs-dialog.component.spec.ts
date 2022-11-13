import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewScannerConfigsDialogComponent } from './new-scanner-configs-dialog.component';

describe('NewScannerConfigsDialogComponent', () => {
  let component: NewScannerConfigsDialogComponent;
  let fixture: ComponentFixture<NewScannerConfigsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewScannerConfigsDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewScannerConfigsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
