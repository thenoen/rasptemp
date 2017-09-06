import { Component, OnInit, Injector, ReflectiveInjector, ComponentFactoryResolver, ComponentFactory, ComponentRef } from '@angular/core';
import { A2HighchartsGraphComponent } from '../a2-highcharts-graph/a2-highcharts-graph.component';
import { GetTemperaturesService } from '../service/get-temperatures-service';

@Component({
  selector: 'app-chart-list',
  templateUrl: './chart-list.component.html',
  styleUrls: ['./chart-list.component.css']
})
export class ChartListComponent implements OnInit {

  chartList: Array<A2HighchartsGraphComponent>;
  chartCompRefs: Array<ComponentRef<A2HighchartsGraphComponent>>;
  chart = A2HighchartsGraphComponent;
  myInjector: Injector;
  factory: ComponentFactory<A2HighchartsGraphComponent>;

  constructor(injector: Injector, private componentFactoryResolver: ComponentFactoryResolver) {
    this.myInjector = ReflectiveInjector.resolveAndCreate([GetTemperaturesService], injector);
    this.factory = this.componentFactoryResolver.resolveComponentFactory(A2HighchartsGraphComponent);
    
  }

  ngOnInit() {
    this.chartList = new Array<A2HighchartsGraphComponent>();
    this.chartList.push(this.factory.create(this.myInjector).instance);
    this.chartList.push(this.factory.create(this.myInjector).instance);
    this.chartList.push(this.factory.create(this.myInjector).instance);
    this.chartList.push(this.factory.create(this.myInjector).instance);

    this.chartCompRefs = new Array<ComponentRef<A2HighchartsGraphComponent>>();

    // this.chart = A2HighchartsGraphComponent;
  }

  addChart() {
    this.chartList.push(this.factory.create(this.myInjector).instance);
    this.chartCompRefs.push(this.factory.create(this.myInjector));
    this.chart = null;

  }

}
