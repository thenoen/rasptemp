import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TemperatureViewerComponent } from './temperature-viewer.component';

describe('TemperatureViewerComponent', () => {
  let component: TemperatureViewerComponent;
  let fixture: ComponentFixture<TemperatureViewerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TemperatureViewerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TemperatureViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
