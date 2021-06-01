import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddressEditDialogComponent } from './address-edit-dialog.component';

describe('AddressEditDialogComponent', () => {
  let component: AddressEditDialogComponent;
  let fixture: ComponentFixture<AddressEditDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddressEditDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddressEditDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
