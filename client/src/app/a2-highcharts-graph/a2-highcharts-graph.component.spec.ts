import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { A2HighchartsGraphComponent } from './a2-highcharts-graph.component';

describe('A2HighchartsGraphComponent', () => {
  let component: A2HighchartsGraphComponent;
  let fixture: ComponentFixture<A2HighchartsGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ A2HighchartsGraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(A2HighchartsGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
