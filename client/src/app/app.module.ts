import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { GraphComponent } from './graph/graph.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { TemperatureViewerComponent } from './temperature-viewer/temperature-viewer.component';

import { GetTemperaturesService } from './service/get-temperatures-service';
import { Ng2GraphComponent } from './ng2-graph/ng2-graph.component';

import { ChartsModule } from 'ng2-charts/ng2-charts';
import { A2HighchartsGraphComponent } from './a2-highcharts-graph/a2-highcharts-graph.component';

import { ChartModule } from 'angular2-highcharts';
import { HighchartsStatic } from 'angular2-highcharts/dist/HighchartsService';

declare var require: any;
export function highchartsFactory() {
  return require('highcharts');
}

@NgModule({
  declarations: [
    AppComponent,
    GraphComponent,
    TemperatureViewerComponent,
    Ng2GraphComponent,
    A2HighchartsGraphComponent
  ],
  imports: [
    BrowserModule,
    NgxChartsModule,
    BrowserAnimationsModule,
    ChartsModule,
    ChartModule
  ],
  providers: [GetTemperaturesService,
   {
    provide: HighchartsStatic,
    useFactory: highchartsFactory
  },],
  bootstrap: [AppComponent]
})
export class AppModule { }
