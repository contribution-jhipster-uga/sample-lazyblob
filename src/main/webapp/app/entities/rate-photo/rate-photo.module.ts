import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LazyblobSharedModule } from 'app/shared/shared.module';
import { RatePhotoComponent } from './rate-photo.component';
import { RatePhotoDetailComponent } from './rate-photo-detail.component';
import { RatePhotoUpdateComponent } from './rate-photo-update.component';
import { RatePhotoDeleteDialogComponent } from './rate-photo-delete-dialog.component';
import { rateRoute } from './rate-photo.route';

@NgModule({
  imports: [LazyblobSharedModule, RouterModule.forChild(rateRoute)],
  declarations: [RatePhotoComponent, RatePhotoDetailComponent, RatePhotoUpdateComponent, RatePhotoDeleteDialogComponent],
  entryComponents: [RatePhotoDeleteDialogComponent]
})
export class LazyblobRatePhotoModule {}
