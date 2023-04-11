import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IcingComponent } from './list/icing.component';
import { IcingDetailComponent } from './detail/icing-detail.component';
import { IcingUpdateComponent } from './update/icing-update.component';
import { IcingDeleteDialogComponent } from './delete/icing-delete-dialog.component';
import { IcingRoutingModule } from './route/icing-routing.module';

@NgModule({
  imports: [SharedModule, IcingRoutingModule],
  declarations: [IcingComponent, IcingDetailComponent, IcingUpdateComponent, IcingDeleteDialogComponent],
})
export class IcingModule {}
