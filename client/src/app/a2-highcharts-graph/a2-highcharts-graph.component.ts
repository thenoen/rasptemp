import {Component, OnInit} from '@angular/core';
import {ChartObject} from 'highcharts';
import {GetTemperaturesService} from "../service/get-temperatures-service";
import {Temperature} from '../model/temperature';

@Component({
    selector: 'app-a2-highcharts-graph',
    templateUrl: './a2-highcharts-graph.component.html',
    styleUrls: ['./a2-highcharts-graph.component.css']
})
export class A2HighchartsGraphComponent implements OnInit {

    options: Object;
    chart: ChartObject;

    constructor(private getTemperatureService: GetTemperaturesService) {
        this.options = {
            chart: {
                type: 'spline'
            },
            title: {text: 'angular2-highcharts example'},
            series: [
                {
                    name: 'temperatures',
                    data: [
                        [Date.UTC(1970, 9, 21), 0],
                        [Date.UTC(1970, 10, 4), 0.28],
                        [Date.UTC(1970, 10, 9), 0.25],
                        [Date.UTC(1970, 10, 27), 0.2],
                        [Date.UTC(1970, 11, 2), 0.28],
                        [Date.UTC(1970, 11, 26), 0.28],
                        [Date.UTC(1970, 11, 29), 0.47],
                        [Date.UTC(1971, 0, 11), 0.79],
                        [Date.UTC(1971, 0, 26), 0.72],
                        [Date.UTC(1971, 1, 3), 1.02],
                        [Date.UTC(1971, 1, 11), 1.12],
                        [Date.UTC(1971, 1, 25), 1.2],
                        [Date.UTC(1971, 2, 11), 1.18],
                        [Date.UTC(1971, 3, 11), 1.19],
                        [Date.UTC(1971, 4, 1), 1.85],
                        [Date.UTC(1971, 4, 5), 2.22],
                        [Date.UTC(1971, 4, 19), 1.15],
                        [Date.UTC(1971, 5, 3), 0]
                    ],
                    allowPointSelect: true
                }
                // , {
                //   name: 's2',
                //   data: [-2, -3, -5, -8, -13],
                //   allowPointSelect: true
                // }
            ],
            xAxis: {
                type: 'datetime',
                dateTimeLabelFormats: { // don't display the dummy year
                    month: '%e. %b',
                    year: '%b'
                },
                title: {
                    text: 'Date'
                }
            },

            plotOptions: {
                spline: {
                    marker: {
                        enabled: true
                    }
                }
            }
        };
    }

    saveChart(chart) {
        console.log(chart)
        this.chart = chart;
    }

    addPoint() {
        // console.log(this.chart);
        // var point: any;
        // point = [Date.UTC(1971, 12, 3), 5];
        // this.chart.series[0].addPoint(point);
        // this.chart.series[1].addPoint(Math.random() * -10);
        this.getTemperatureService.getTemperatures().then((temperatures) => this.changeSeries(temperatures))
    }

    onPointSelect(point) {
        alert(`${point.y} is selected`);
    }

    onSeriesHide(series) {
        alert(`${series.name} is selected`);
    }

    ngOnInit() {
    }

    changeSeries(temperatures: Temperature[]) {

        console.log("this.chart:")
        console.log(this.chart)
        console.log("this.chart.addSeries:")
        console.log(this.chart.addSeries)

        this.chart.series[0].remove();

        this.chart.addSeries({
            name: 's1',
            data: this.createDataArray(temperatures),
            allowPointSelect: true
        })

    }

    createDataArray(temperatures: Temperature[]): any[] {
        var data: any[];

        data = [];

        for (var i = 1; i < temperatures.length; i++) {
            data.push([Date.parse(temperatures[i].date.toUTCString()), temperatures[i].degrees])
        }

        return data;
    }

}
