import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewCompanyDialogComponent } from './new-company-dialog.component';

describe('NewCompanyDialogComponent', () => {
  let component: NewCompanyDialogComponent;
  let fixture: ComponentFixture<NewCompanyDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewCompanyDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewCompanyDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
