import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFlavor } from '../flavor.model';
import { FlavorService } from '../service/flavor.service';

@Injectable({ providedIn: 'root' })
export class FlavorRoutingResolveService implements Resolve<IFlavor | null> {
  constructor(protected service: FlavorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFlavor | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((flavor: HttpResponse<IFlavor>) => {
          if (flavor.body) {
            return of(flavor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
