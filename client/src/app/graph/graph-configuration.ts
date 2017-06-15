import { NgxChartsModule } from '@swimlane/ngx-charts';
import * as shape from 'd3-shape';

export class GraphConfiguration {

    curves: any = {
        Basis: shape.curveBasis,
        'Basis Closed': shape.curveBasisClosed,
        Bundle: shape.curveBundle.beta(1),
        Cardinal: shape.curveCardinal,
        'Cardinal Closed': shape.curveCardinalClosed,
        'Catmull Rom': shape.curveCatmullRom,
        'Catmull Rom Closed': shape.curveCatmullRomClosed,
        Linear: shape.curveLinear,
        'Linear Closed': shape.curveLinearClosed,
        'Monotone X': shape.curveMonotoneX,
        'Monotone Y': shape.curveMonotoneY,
        Natural: shape.curveNatural,
        Step: shape.curveStep,
        'Step After': shape.curveStepAfter,
        'Step Before': shape.curveStepBefore,
        default: shape.curveLinear
    };

    // line interpolation
    curveType: string = 'Natural';
    curve: any = this.curves[this.curveType];

    view: any[] = [700, 400];

    // options
    showXAxis = true;
    showYAxis = true;
    gradient = false;
    showLegend = true;
    showXAxisLabel = true;
    xAxisLabel = 'Country';
    showYAxisLabel = true;
    yAxisLabel = 'Population';

    colorScheme = {
        domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
    };

    // line, area
    autoScale = true;

}
