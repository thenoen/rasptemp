import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class GetTemperaturesService {

  constructor(private http: HttpClient) {
  }

  getTemperatures(hours: number, groupSize: number): Promise<any[]> {
    return this.http.get("/lastHours/" + hours + "/" + groupSize).toPromise()
      .then(data => this.transform(data))
  }

  getLastTemperature() {
    return this.http.get("/latestValue").toPromise()
      .then(data => data["degrees"])
  }

  private transform(data: any): any[] {
    // console.log(data);
    const result: any = []
    for (let entry of data) {
      result.push({name: new Date(entry["dateMeasured"]), value: entry["degrees"]})
    }
    console.log(result);
    return result;
  }
}

