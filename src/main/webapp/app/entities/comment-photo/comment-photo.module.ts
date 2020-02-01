import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LazyblobSharedModule } from 'app/shared/shared.module';
import { CommentPhotoComponent } from './comment-photo.component';
import { CommentPhotoDetailComponent } from './comment-photo-detail.component';
import { CommentPhotoUpdateComponent } from './comment-photo-update.component';
import { CommentPhotoDeleteDialogComponent } from './comment-photo-delete-dialog.component';
import { commentRoute } from './comment-photo.route';

@NgModule({
  imports: [LazyblobSharedModule, RouterModule.forChild(commentRoute)],
  declarations: [CommentPhotoComponent, CommentPhotoDetailComponent, CommentPhotoUpdateComponent, CommentPhotoDeleteDialogComponent],
  entryComponents: [CommentPhotoDeleteDialogComponent]
})
export class LazyblobCommentPhotoModule {}
