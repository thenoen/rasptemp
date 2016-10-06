import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { TemperatureComponent } from './temperature.component';

import { Ng2HighchartsModule } from 'ng2-highcharts';

@NgModule({
    imports: [CommonModule, SharedModule, Ng2HighchartsModule],
    declarations: [TemperatureComponent],
    exports: [TemperatureComponent]
})

export class TemperatureModule { }
