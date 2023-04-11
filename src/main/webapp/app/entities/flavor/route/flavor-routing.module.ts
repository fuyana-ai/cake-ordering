import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FlavorComponent } from '../list/flavor.component';
import { FlavorDetailComponent } from '../detail/flavor-detail.component';
import { FlavorUpdateComponent } from '../update/flavor-update.component';
import { FlavorRoutingResolveService } from './flavor-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const flavorRoute: Routes = [
  {
    path: '',
    component: FlavorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FlavorDetailComponent,
    resolve: {
      flavor: FlavorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FlavorUpdateComponent,
    resolve: {
      flavor: FlavorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FlavorUpdateComponent,
    resolve: {
      flavor: FlavorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(flavorRoute)],
  exports: [RouterModule],
})
export class FlavorRoutingModule {}
