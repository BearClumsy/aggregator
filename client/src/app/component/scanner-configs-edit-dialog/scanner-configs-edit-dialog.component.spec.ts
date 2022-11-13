import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScannerConfigsEditDialogComponent } from './scanner-configs-edit-dialog.component';

describe('ScannerConfigsEditDialogComponent', () => {
  let component: ScannerConfigsEditDialogComponent;
  let fixture: ComponentFixture<ScannerConfigsEditDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScannerConfigsEditDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScannerConfigsEditDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
