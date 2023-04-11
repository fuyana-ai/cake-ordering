import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CakeComponent } from './list/cake.component';
import { CakeDetailComponent } from './detail/cake-detail.component';
import { CakeUpdateComponent } from './update/cake-update.component';
import { CakeDeleteDialogComponent } from './delete/cake-delete-dialog.component';
import { CakeRoutingModule } from './route/cake-routing.module';

@NgModule({
  imports: [SharedModule, CakeRoutingModule],
  declarations: [CakeComponent, CakeDetailComponent, CakeUpdateComponent, CakeDeleteDialogComponent],
})
export class CakeModule {}
