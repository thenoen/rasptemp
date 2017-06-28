import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { GraphComponent } from './graph/graph.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { TemperatureViewerComponent } from './temperature-viewer/temperature-viewer.component';

import { GetTemperaturesService } from './service/get-temperatures-service';

@NgModule({
  declarations: [
    AppComponent,
    GraphComponent,
    TemperatureViewerComponent
  ],
  imports: [
    BrowserModule,
    NgxChartsModule,
    BrowserAnimationsModule
  ],
  providers: [GetTemperaturesService],
  bootstrap: [AppComponent]
})
export class AppModule { }
