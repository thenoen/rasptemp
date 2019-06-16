import {Component, ViewChild, ElementRef} from '@angular/core';
import * as shape from 'd3-shape';
import {GetTemperaturesService} from './service/get-temperatures-service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'client-next';

  view: any[] = [700, 400];

  // options
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = true;
  xAxisLabel = 'Date';
  showYAxisLabel = true;
  yAxisLabel = 'Temperature';
  timeline = true;
  autoScale = true;
  curve = shape.curveBasis;
  legendPosition = "below";

  colorScheme = {
    domain: ['#144aad']
  };

  multi: any[] = [
    {
      name: 'Temperatures',
      series: []
    }
  ];

  constructor(public temperaturesService: GetTemperaturesService) {
  }

  public reloadData() {
    this.temperaturesService.getTemperatures3()
      .then(data => this.multi = [{name: "Temperature", series: data}]);
    console.log(this.multi);
  }

  public onSelect(data: any) {
    console.log(data);
  }

}
