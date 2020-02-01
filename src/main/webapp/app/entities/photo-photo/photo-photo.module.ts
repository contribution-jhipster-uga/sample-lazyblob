import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LazyblobSharedModule } from 'app/shared/shared.module';
import { PhotoPhotoComponent } from './photo-photo.component';
import { PhotoPhotoDetailComponent } from './photo-photo-detail.component';
import { PhotoPhotoUpdateComponent } from './photo-photo-update.component';
import { PhotoPhotoDeleteDialogComponent } from './photo-photo-delete-dialog.component';
import { photoRoute } from './photo-photo.route';

@NgModule({
  imports: [LazyblobSharedModule, RouterModule.forChild(photoRoute)],
  declarations: [PhotoPhotoComponent, PhotoPhotoDetailComponent, PhotoPhotoUpdateComponent, PhotoPhotoDeleteDialogComponent],
  entryComponents: [PhotoPhotoDeleteDialogComponent]
})
export class LazyblobPhotoPhotoModule {}
