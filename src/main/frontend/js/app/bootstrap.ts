///<reference path="../../../../../node_modules/angular2/typings/browser.d.ts"/>

import {bootstrap} from '../../../../../node_modules/@angular/platform-browser-dynamic';
import {PonyRacerApp} from './rasptemp.component';
bootstrap(PonyRacerApp).catch(err => console.log(err)); // useful to catch the errors
