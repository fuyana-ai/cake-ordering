import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FlavorComponent } from './list/flavor.component';
import { FlavorDetailComponent } from './detail/flavor-detail.component';
import { FlavorUpdateComponent } from './update/flavor-update.component';
import { FlavorDeleteDialogComponent } from './delete/flavor-delete-dialog.component';
import { FlavorRoutingModule } from './route/flavor-routing.module';

@NgModule({
  imports: [SharedModule, FlavorRoutingModule],
  declarations: [FlavorComponent, FlavorDetailComponent, FlavorUpdateComponent, FlavorDeleteDialogComponent],
})
export class FlavorModule {}
