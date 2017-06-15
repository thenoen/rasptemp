import { Component, OnInit } from '@angular/core';

import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { GraphConfiguration } from './graph-configuration'



@Component({
  selector: 'app-graph',
  templateUrl: './graph.component.html',
  styleUrls: ['./graph.component.css'],
  inputs: ['graphConfiguration', 'data'],
})
export class GraphComponent implements OnInit {

  graphConfiguration: GraphConfiguration;
  data: any;

  constructor() { }

  ngOnInit() {
  }

}
