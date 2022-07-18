import {AfterViewInit, Component, OnInit} from '@angular/core';
import {TemperatureService} from "./service/temperature.service";
import {EventService} from "./service/event.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, AfterViewInit {
  latestValue: string = '';

  constructor(private temperatureService: TemperatureService,
              private eventService: EventService) {
  }

  public getLatestValue(): void {
    this.temperatureService.getLastTemperature().subscribe(
      data => this.latestValue = data['degrees'],
      // data => console.log(data)
    );
    this.eventService.refresh();
  }

  ngOnInit(): void {
    // this.getLatestValue();
  }

  ngAfterViewInit(): void {
    this.getLatestValue();
  }
}
