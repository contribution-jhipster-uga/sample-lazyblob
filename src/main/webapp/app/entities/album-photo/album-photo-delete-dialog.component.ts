import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAlbumPhoto } from 'app/shared/model/album-photo.model';
import { AlbumPhotoService } from './album-photo.service';

@Component({
  templateUrl: './album-photo-delete-dialog.component.html'
})
export class AlbumPhotoDeleteDialogComponent {
  album: IAlbumPhoto;

  constructor(protected albumService: AlbumPhotoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.albumService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'albumListModification',
        content: 'Deleted an album'
      });
      this.activeModal.dismiss(true);
    });
  }
}
