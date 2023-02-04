import {Component, ElementRef, Input, ViewChild} from '@angular/core';
import {Color, ScaleType} from '@swimlane/ngx-charts';
import {EventService} from '../service/event.service';
import {TemperatureService} from '../service/temperature.service';
import {curveCatmullRom} from 'd3-shape';

@Component({
  selector: 'app-temperature-chart',
  templateUrl: './temperature-chart.component.html',
  styleUrls: ['./temperature-chart.component.scss']
})
export class TemperatureChartComponent {

  @ViewChild("chartContainer")
  private chartContainer?: ElementRef<HTMLElement>;

  @Input()
  hoursRange: number = 0;

  @Input()
  groupSize: number = 0;

  view: [number, number] = [400, 200];

  // options
  legend: boolean = false;
  showLabels: boolean = true;
  animations: boolean = true;
  xAxis: boolean = true;
  yAxis: boolean = true;
  showYAxisLabel: boolean = true;
  showXAxisLabel: boolean = false;
  xAxisLabel: string = 'x-label';
  yAxisLabel: string = '℃';
  timeline: boolean = false;
  rangeBottom: number = -100;
  rangeTop: number = 100;
  curve: any = curveCatmullRom.alpha(0.5)

  colorScheme: Color = {
    name: "default",
    selectable: true,
    group: ScaleType.Linear,
    domain: ['#054f86', '#E44D25', '#CFC0BB', '#7aa3e5', '#a8385d', '#aae3f5']
  };

  data: any[] = [{
    name: '',
    series: []
  }];

  constructor(chartContainer: ElementRef,
              private eventService: EventService,
              private temperatureService: TemperatureService) {

    let subscription = eventService.onRefresh({
      next: (refreshEvent) => {
        temperatureService.getTemperatures(this.hoursRange, this.groupSize)
          .then(data => this.data[0].series = data)
          .then(() => this.data = [...this.data])
          .then(() => this.findMinMax())
        // console.log(this.data);
        // this.findMinMax();
        this.onResize(undefined);
      },
      error: (refresh: Error) => console.log("error: " + refresh),
      complete: () => subscription.unsubscribe()
    });
  }

  private readonly minimalRange = 3;

  private findMinMax(): void {
    // console.log("data:");
    // console.log(this.data[0].series);
    let min: number = Number.MAX_VALUE;
    this.data[0].series.forEach((d: any) => {
      // console.log(d.value);
      if (d.value < min) {
        min = d.value;
      }
    });

    let max: number = Number.MIN_VALUE;
    this.data[0].series.forEach((d: any) => {
      // console.log(d.value);
      if (d.value > max) {
        max = d.value;
      }
    });

    let diff = max - min;
    if (diff < this.minimalRange) {
      max += (this.minimalRange-diff)/2;
      min -= (this.minimalRange-diff)/2;
    }
    this.rangeBottom = min;
    this.rangeTop = max;
  }

  ngOnInit() {
    this.xAxisLabel = `Last ${ this.hoursRange } hours`;
  }

  ngAfterContentInit() {
    this.onResize(undefined);
  }

  onResize(data: any) {
    if (this.chartContainer != undefined) {
      console.log(this.chartContainer.nativeElement.clientWidth + " / " + this.chartContainer.nativeElement.clientWidth * (9 / 16));
      this.view = [this.chartContainer.nativeElement.clientWidth, this.chartContainer.nativeElement.clientWidth * (9 / 16)];
      // this.view = [this.chartContainer.nativeElement.clientWidth, this.chartContainer.nativeElement.clientHeight];
      // this.view = [this.chartContainer.nativeElement.clientWidth, this.chartContainer.nativeElement.clientWidth * 0.5];
    } else {
      console.log("chartContainer undefined");
    }
  }

  onSelect(data: any): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  onActivate(data: any): void {
    console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data: any): void {
    console.log('Deactivate', JSON.parse(JSON.stringify(data)));
  }

  loadData(): void {
    this.onResize(undefined);
  }

}
