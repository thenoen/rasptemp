import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';

import { GetTemperaturesService } from './service/get-temperatures-service';

import { A2HighchartsGraphComponent } from './a2-highcharts-graph/a2-highcharts-graph.component';

import { ChartModule } from 'angular2-highcharts';
import { HighchartsStatic } from 'angular2-highcharts/dist/HighchartsService';
import { ChartListComponent } from './chart-list/chart-list.component';

declare var require: any;
export function highchartsFactory() {
  return require('highcharts');
}

@NgModule({
  declarations: [
    AppComponent,
    A2HighchartsGraphComponent,
    ChartListComponent
  ],
  entryComponents: [
    A2HighchartsGraphComponent
  ],
  imports: [
    BrowserModule,
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
