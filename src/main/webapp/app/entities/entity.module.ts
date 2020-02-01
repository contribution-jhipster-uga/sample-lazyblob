import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'photo-photo',
        loadChildren: () => import('./photo-photo/photo-photo.module').then(m => m.LazyblobPhotoPhotoModule)
      },
      {
        path: 'album-photo',
        loadChildren: () => import('./album-photo/album-photo.module').then(m => m.LazyblobAlbumPhotoModule)
      },
      {
        path: 'comment-photo',
        loadChildren: () => import('./comment-photo/comment-photo.module').then(m => m.LazyblobCommentPhotoModule)
      },
      {
        path: 'rate-photo',
        loadChildren: () => import('./rate-photo/rate-photo.module').then(m => m.LazyblobRatePhotoModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class LazyblobEntityModule {}
