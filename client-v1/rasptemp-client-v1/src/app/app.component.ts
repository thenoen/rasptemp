import {Component} from '@angular/core';
import {TemperatureService} from "./service/temperature.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  latestValue: string = '';

  constructor(private temperatureService: TemperatureService) {
  }

  public getLatestValue(): void {
    this.temperatureService.getLastTemperature().subscribe(
      data => this.latestValue = data['degrees'],
      // data => console.log(data)
    );
  }
}
