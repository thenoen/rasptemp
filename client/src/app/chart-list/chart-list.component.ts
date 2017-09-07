import {Component, OnInit, ViewContainerRef,Injector, ComponentFactoryResolver, ReflectiveInjector, ComponentFactory, ViewChild} from '@angular/core';
import {A2HighchartsGraphComponent} from '../a2-highcharts-graph/a2-highcharts-graph.component';
import {GetTemperaturesService} from '../service/get-temperatures-service';

@Component({
    selector: 'app-chart-list',
    templateUrl: './chart-list.component.html',
    styleUrls: ['./chart-list.component.css']
})
export class ChartListComponent implements OnInit {

    private getTemperaturesServiceInjector: Injector;
    private factory: ComponentFactory<A2HighchartsGraphComponent>;

    @ViewChild('target', {read: ViewContainerRef}) target;

    constructor(private injector: Injector,
                private componentFactoryResolver: ComponentFactoryResolver) {
    }

    appendChartComponent() {
        this.getTemperaturesServiceInjector = ReflectiveInjector.resolveAndCreate([GetTemperaturesService], this.injector);
        this.factory = this.componentFactoryResolver.resolveComponentFactory(A2HighchartsGraphComponent);
        this.target.createComponent(this.factory, this.target.length, this.getTemperaturesServiceInjector)
    }

    ngOnInit() {
        this.appendChartComponent();
    }

    addChart() {
        this.appendChartComponent();
    }

}
