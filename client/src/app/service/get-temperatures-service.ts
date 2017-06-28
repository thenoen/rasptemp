import { Injectable } from '@angular/core';
import { Temperature } from './../model/temperature';

@Injectable()
export class GetTemperaturesService {

    private temperatures: Temperature[] = [];

    getTemperatures(): Promise<Temperature[]> {

        let temperature: Temperature;

        for (var i = 0; i < 100; i++) {
            temperature = new Temperature();
            temperature.date = new Date("February 4, 2016 10:13:00");
            temperature.date.setMinutes(temperature.date.getMinutes() + i);
            temperature.degrees = Math.floor(Math.random() *100) / 10;
            this.temperatures.push(temperature);
        }

        console.log("created server response");
        console.log(this.temperatures);
        return Promise.resolve(this.temperatures);
    }

}

