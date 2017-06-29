import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Ng2GraphComponent } from './ng2-graph.component';

describe('Ng2GraphComponent', () => {
  let component: Ng2GraphComponent;
  let fixture: ComponentFixture<Ng2GraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Ng2GraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Ng2GraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
