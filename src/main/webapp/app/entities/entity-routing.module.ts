import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'color',
        data: { pageTitle: 'cakeOrderingApplicationApp.color.home.title' },
        loadChildren: () => import('./color/color.module').then(m => m.ColorModule),
      },
      {
        path: 'icing',
        data: { pageTitle: 'cakeOrderingApplicationApp.icing.home.title' },
        loadChildren: () => import('./icing/icing.module').then(m => m.IcingModule),
      },
      {
        path: 'flavor',
        data: { pageTitle: 'cakeOrderingApplicationApp.flavor.home.title' },
        loadChildren: () => import('./flavor/flavor.module').then(m => m.FlavorModule),
      },
      {
        path: 'cake',
        data: { pageTitle: 'cakeOrderingApplicationApp.cake.home.title' },
        loadChildren: () => import('./cake/cake.module').then(m => m.CakeModule),
      },
      {
        path: 'order',
        data: { pageTitle: 'cakeOrderingApplicationApp.order.home.title' },
        loadChildren: () => import('./order/order.module').then(m => m.OrderModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
