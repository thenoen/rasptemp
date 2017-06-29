import { Component, OnInit, ViewChild } from '@angular/core';
import { Temperature } from './../model/temperature';
import {BaseChartDirective} from 'ng2-charts/ng2-charts';

@Component({
  selector: 'app-ng2-graph',
  templateUrl: './ng2-graph.component.html',
  styleUrls: ['./ng2-graph.component.css']
})
export class Ng2GraphComponent implements OnInit {

  @ViewChild(BaseChartDirective) chart: BaseChartDirective;

  // private temperatures: Array<Temperature>;
  private temperatures: Temperature[] = [];

  constructor() { }

  ngOnInit() {

  }

  // lineChart
  public lineChartData: Array<any> = [
    // { data: [65, 59, 80, 81, 56, 55, 40], label: 'Series A' },
    // { data: [28, 48, 40, 19, 86, 27, 90], label: 'Series B' },
    { data: [18, 48, 77, 9, 100, 27, 40], label: 'Series C' }
  ];
  public lineChartLabels: Array<any> = ['January', 'February', 'March', 'April', 'May', 'June', 'July'];
  public lineChartOptions: any = {
    responsive: true
  };
  public lineChartColors: Array<any> = [
    { // grey
      backgroundColor: 'rgba(148,159,177,0.2)',
      borderColor: 'rgba(148,159,177,1)',
      pointBackgroundColor: 'rgba(148,159,177,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(148,159,177,0.8)'
    },
    { // dark grey
      backgroundColor: 'rgba(77,83,96,0.2)',
      borderColor: 'rgba(77,83,96,1)',
      pointBackgroundColor: 'rgba(77,83,96,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(77,83,96,1)'
    },
    { // grey
      backgroundColor: 'rgba(148,159,177,0.2)',
      borderColor: 'rgba(148,159,177,1)',
      pointBackgroundColor: 'rgba(148,159,177,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(148,159,177,0.8)'
    }
  ];
  public lineChartLegend: boolean = true;
  public lineChartType: string = 'line';

  public randomize(): void {
    let _lineChartData: Array<any> = new Array(this.lineChartData.length);
    for (let i = 0; i < this.lineChartData.length; i++) {
      _lineChartData[i] = { data: new Array(this.lineChartData[i].data.length), label: this.lineChartData[i].label };
      for (let j = 0; j < this.lineChartData[i].data.length; j++) {
        _lineChartData[i].data[j] = Math.floor((Math.random() * 100) + 1);
      }
    }
    this.lineChartData = _lineChartData;


    let temperature: Temperature;
    let nrOfValues: number;
    nrOfValues = Math.floor(Math.random() * 30);

    for (var i = 0; i < nrOfValues; i++) {
      temperature = new Temperature();
      temperature.date = new Date("February 4, 2016 10:13:00");
      temperature.date.setMinutes(temperature.date.getMinutes() + i);
      temperature.degrees = Math.floor(Math.random() * 100) / 10;
      this.temperatures.push(temperature);
    }

    _lineChartData = new Array(1);
    let _lineChartLabels: Array<any> = new Array(this.temperatures.length);
    for (let i = 0; i < 1; i++) {
      _lineChartData[i] = { data: new Array(this.temperatures.length), label: "temperatures" };
      this.lineChartLabels = new Array(this.temperatures.length);
      for (let j = 0; j < this.temperatures.length; j++) {
        _lineChartData[i].data[j] = this.temperatures[j].degrees;
        this.lineChartLabels[j] = this.temperatures[j].date.toDateString;
      }
    }
    this.lineChartData = _lineChartData;
    this.lineChartLabels = _lineChartLabels;

    setTimeout(() => {
            if (this.chart && this.chart.chart && this.chart.chart.config) {
                this.chart.chart.config.data.labels = this.lineChartLabels;
                this.chart.chart.update();
            }
        });
  }

  // events
  public chartClicked(e: any): void {
    console.log(e);
  }

  public chartHovered(e: any): void {
    console.log(e);
  }

}
