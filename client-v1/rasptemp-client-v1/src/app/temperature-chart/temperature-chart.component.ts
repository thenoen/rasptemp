import {Component, ElementRef, ViewChild} from '@angular/core';
import {Color, ScaleType} from "@swimlane/ngx-charts";
import {multi} from "./data";

@Component({
  selector: 'app-temperature-chart',
  templateUrl: './temperature-chart.component.html',
  styleUrls: ['./temperature-chart.component.scss']
})
export class TemperatureChartComponent {
  multi: any[] = [];

  @ViewChild("chartContainer")
  private chartContainer?: ElementRef<HTMLElement>;

  view: [number, number] = [400, 200];

  // options
  legend: boolean = true;
  showLabels: boolean = true;
  animations: boolean = true;
  xAxis: boolean = true;
  yAxis: boolean = true;
  showYAxisLabel: boolean = true;
  showXAxisLabel: boolean = true;
  xAxisLabel: string = 'Year';
  yAxisLabel: string = 'Population';
  timeline: boolean = true;

  colorScheme: Color = {
    name: "default",
    selectable: true,
    group: ScaleType.Linear,
    domain: ['#5AA454', '#E44D25', '#CFC0BB', '#7aa3e5', '#a8385d', '#aae3f5']
  };

  constructor(chartContainer: ElementRef) {
    // this.chartContainer = chartContainer;
    this.multi = multi;
    // Object.assign(this, {multi});
  }

  ngAfterContentInit() {
    this.onResize(undefined);
  }

  onResize(data: any) {
    if (this.chartContainer != undefined) {
      console.log(this.chartContainer.nativeElement.clientWidth + " / " + this.chartContainer.nativeElement.clientWidth * (9/16));
      this.view = [this.chartContainer.nativeElement.clientWidth, this.chartContainer.nativeElement.clientWidth*(9/16)];
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
