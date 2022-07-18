import {EventEmitter, Injectable} from '@angular/core';
import {RefreshEvent} from "../model/RefreshEvent";
import {Observer, Subscription} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private refreshEvent$: EventEmitter<RefreshEvent>;

  constructor() {
    this.refreshEvent$ = new EventEmitter();
  }

  public refresh(): void {
    this.refreshEvent$.emit(new RefreshEvent());
  }

  public onRefresh(observer: Observer<RefreshEvent>): Subscription {
    return this.refreshEvent$.subscribe(observer);
  }
}
