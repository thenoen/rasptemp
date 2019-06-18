import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import * as shape from 'd3-shape';
import {GetTemperaturesService} from './service/get-temperatures-service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit{
  title = 'client-next';

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
  curve = shape.curveBasis;
  legendPosition = "below";

  colorScheme = {
    domain: ['#144aad','#03ff8e']
  };

  data4h: any[] = [
    {
      name: 'Temperatures - 4h',
      series: []
    }
  ];

  data12h: any[] = [
    {
      name: 'Temperatures - 12h',
      series: []
    }
  ];

  @ViewChild('xxx',{static: false})
  private xxx: ElementRef;

  constructor(private temperaturesService: GetTemperaturesService,
              private el:ElementRef) {


  }

  ngOnInit(): void {
    // this.view[0] = this.xxx.nativeElement.offsetHeight;
  }

  ngAfterViewInit(): void {
    console.log("ngAfterViewInit");
    this.view[0] = this.xxx.nativeElement.offsetWidth;
    this.view[1] = this.xxx.nativeElement.offsetHeight;
    console.log(this.view);
  }


  public reload4HoursData() {
    this.temperaturesService.getTemperatures3(4)
      .then(data => this.data4h[0].series = data)
      .then(() => this.data4h = [...this.data4h]);
  }

  public reload12HoursData() {
    this.temperaturesService.getTemperatures3(12)
      .then(data => this.data12h[0].series = data)
      .then(() => this.data12h = [...this.data12h]);
  }

  public onSelect(data: any) {
    console.log(data);
  }
}
