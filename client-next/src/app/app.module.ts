import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {GetTemperaturesService} from './service/get-temperatures-service';

import {NgxChartsModule} from '@swimlane/ngx-charts';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    NgxChartsModule,
    BrowserAnimationsModule,
    HttpClientModule
  ],
  providers: [GetTemperaturesService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
