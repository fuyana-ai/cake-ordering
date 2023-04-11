import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICake } from '../cake.model';
import { CakeService } from '../service/cake.service';

@Injectable({ providedIn: 'root' })
export class CakeRoutingResolveService implements Resolve<ICake | null> {
  constructor(protected service: CakeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICake | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cake: HttpResponse<ICake>) => {
          if (cake.body) {
            return of(cake.body);
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
