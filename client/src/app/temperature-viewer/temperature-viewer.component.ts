import { Component, OnInit } from '@angular/core';
import { GraphConfiguration } from '../graph/graph-configuration';
import { GetTemperaturesService } from '../service/get-temperatures-service';

import { Temperature } from './../model/temperature';

@Component({
  selector: 'app-temperature-viewer',
  templateUrl: './temperature-viewer.component.html',
  styleUrls: ['./temperature-viewer.component.css']
})
export class TemperatureViewerComponent implements OnInit {

  graphConfiguration: GraphConfiguration = new GraphConfiguration();

  multidata: any[] = [
    {
      "name": "Germany",
      "series": [
        {
          "name": "2010",
          "value": 7300000
        },
        {
          "name": "2011",
          "value": 8940000
        },
        {
          "name": "2012",
          "value": 7940000
        },
        {
          "name": "2013",
          "value": 6940000
        },
        {
          "name": "2014",
          "value": 7540000
        }
      ]
    }
  ];

  constructor(private getTemperaturesService: GetTemperaturesService) {
    console.log("constructor: " + this.graphConfiguration);
  }

  ngOnInit() {
    console.log("onInit: " + this.graphConfiguration);
    this.startTemperaturesLoading();
  }

  private startTemperaturesLoading(): void {
    let x: Temperature[];
    this.getTemperaturesService.getTemperatures().then((temperatures) => this.handleResponse(temperatures));
  }

  private handleResponse(temperatures: Temperature[]): void {
    console.log("handling response");
    console.log(temperatures);

    let data: Array<any> = new Array();

    for (var i = 0; i < temperatures.length; i++) {
      data.push({ "name": temperatures[i].date.toString(), "value": temperatures[i].degrees });
    }

    
    this.multidata = [{ "name": "Temperatures", "series": data }];
    console.log("updated multidata");
    console.log(this.multidata);
  }

}
