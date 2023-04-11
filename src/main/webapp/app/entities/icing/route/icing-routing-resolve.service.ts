import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIcing } from '../icing.model';
import { IcingService } from '../service/icing.service';

@Injectable({ providedIn: 'root' })
export class IcingRoutingResolveService implements Resolve<IIcing | null> {
  constructor(protected service: IcingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIcing | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((icing: HttpResponse<IIcing>) => {
          if (icing.body) {
            return of(icing.body);
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
