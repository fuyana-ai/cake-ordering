import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IcingComponent } from '../list/icing.component';
import { IcingDetailComponent } from '../detail/icing-detail.component';
import { IcingUpdateComponent } from '../update/icing-update.component';
import { IcingRoutingResolveService } from './icing-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const icingRoute: Routes = [
  {
    path: '',
    component: IcingComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IcingDetailComponent,
    resolve: {
      icing: IcingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IcingUpdateComponent,
    resolve: {
      icing: IcingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IcingUpdateComponent,
    resolve: {
      icing: IcingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(icingRoute)],
  exports: [RouterModule],
})
export class IcingRoutingModule {}
