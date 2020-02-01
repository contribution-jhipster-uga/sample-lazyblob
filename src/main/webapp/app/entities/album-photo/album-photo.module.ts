import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LazyblobSharedModule } from 'app/shared/shared.module';
import { AlbumPhotoComponent } from './album-photo.component';
import { AlbumPhotoDetailComponent } from './album-photo-detail.component';
import { AlbumPhotoUpdateComponent } from './album-photo-update.component';
import { AlbumPhotoDeleteDialogComponent } from './album-photo-delete-dialog.component';
import { albumRoute } from './album-photo.route';

@NgModule({
  imports: [LazyblobSharedModule, RouterModule.forChild(albumRoute)],
  declarations: [AlbumPhotoComponent, AlbumPhotoDetailComponent, AlbumPhotoUpdateComponent, AlbumPhotoDeleteDialogComponent],
  entryComponents: [AlbumPhotoDeleteDialogComponent]
})
export class LazyblobAlbumPhotoModule {}
