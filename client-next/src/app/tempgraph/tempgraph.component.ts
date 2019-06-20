import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {GetTemperaturesService} from "../service/get-temperatures-service";
import * as shape from 'd3-shape';

@Component({
  selector: 'app-tempgraph',
  templateUrl: './tempgraph.component.html',
  styleUrls: ['./tempgraph.component.css']
})
export class TempgraphComponent implements OnInit, AfterViewInit {

  @Input() hours: number;
  @Input() groupSize: number;

  view: any[] = [];

  // options
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = true;
  xAxisLabel = 'Date';
  showYAxisLabel = true;
  yAxisLabel = 'Temperature';
  timeline = false;
  autoScale = true;
  curve = shape.curveCatmullRom;
  legendPosition = "below";

  colorScheme = {
    domain: ['#144aad','#03ff8e']
  };

  data: any[] = [
    {
      name: '',
      series: [{"name":"0", "value":"0"},{"name":"1", "value":"0"}]
    }
  ];

  currentTemperature: number;

  @ViewChild('xxx',{static: false})
  private xxx: ElementRef;

  constructor(private temperaturesService: GetTemperaturesService,
              private el:ElementRef) {}

  ngOnInit(): void {
    this.data[0].name = "Temperature during last " + this.hours + " hours";
  }

  ngAfterViewInit(): void {
    console.log("ngAfterViewInit");
    this.view[0] = this.xxx.nativeElement.offsetWidth;
    this.view[1] = this.xxx.nativeElement.offsetHeight;
    // this.view[0] = 800;
    this.view[1] = 300;
    console.log(this.view);
  }


  public reloadData() {
    this.temperaturesService.getTemperatures(this.hours, this.groupSize)
      .then(data => this.data[0].series = data)
      .then(() => this.data = [...this.data]);

    this.temperaturesService.getLastTemperature()
      .then(value =>this.currentTemperature = value);
  }

  public onSelect(data: any) {
    console.log(data);
  }

}
