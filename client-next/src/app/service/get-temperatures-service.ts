import {Injectable} from '@angular/core';
import {Temperature} from '../model/temperature';
import {HttpClient} from '@angular/common/http';
import {__await} from "tslib";

@Injectable()
export class GetTemperaturesService {

  constructor(private http: HttpClient) {
  }

  getTemperatures3(): Promise<any[]> {
    let response: any;
    return this.http.get("/lastPeriod").toPromise()
      .then(data => this.transform(data))
  }

  private transform(data: any): any[] {
    console.log(data);
    const result: any = []
    for (let entry of data) {
      result.push({name: new Date(entry["dateMeasured"]), value: entry["degrees"]})
    }
    console.log(result);
    return result;
  }
}

