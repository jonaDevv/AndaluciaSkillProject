import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GanadoresComponent } from './ganadores.component';

describe('GanadoresComponent', () => {
  let component: GanadoresComponent;
  let fixture: ComponentFixture<GanadoresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GanadoresComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GanadoresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
