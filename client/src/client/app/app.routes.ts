import { Routes } from '@angular/router';

import { AboutRoutes } from './about/index';
import { HomeRoutes } from './home/index';
import { ChartsRoutes } from './charts/index';
import { TemperatureRoutes } from "./temperature/temperature.routes";

export const routes: Routes = [
  ...HomeRoutes,
  ...AboutRoutes,
  ...ChartsRoutes,
  ...TemperatureRoutes
];
