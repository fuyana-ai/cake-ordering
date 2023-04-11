import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CakeComponent } from '../list/cake.component';
import { CakeDetailComponent } from '../detail/cake-detail.component';
import { CakeUpdateComponent } from '../update/cake-update.component';
import { CakeRoutingResolveService } from './cake-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const cakeRoute: Routes = [
  {
    path: '',
    component: CakeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CakeDetailComponent,
    resolve: {
      cake: CakeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CakeUpdateComponent,
    resolve: {
      cake: CakeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CakeUpdateComponent,
    resolve: {
      cake: CakeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cakeRoute)],
  exports: [RouterModule],
})
export class CakeRoutingModule {}
