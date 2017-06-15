import { Component, OnInit } from '@angular/core';
import { GraphConfiguration } from '../graph/graph-configuration';

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
    },

    {
      "name": "USA",
      "series": [
        {
          "name": "2010",
          "value": 7870000
        },
        {
          "name": "2011",
          "value": 8270000
        }
      ]
    },

    {
      "name": "France",
      "series": [
        {
          "name": "2010",
          "value": 5000002
        },
        {
          "name": "2011",
          "value": 5800000
        }
      ]
    }
  ];

  constructor() {
    console.log("constructor: " + this.graphConfiguration);
  }

  ngOnInit() {
    console.log("onInit: " + this.graphConfiguration);
  }

}
