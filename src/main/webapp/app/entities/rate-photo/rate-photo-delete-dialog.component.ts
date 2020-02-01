import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRatePhoto } from 'app/shared/model/rate-photo.model';
import { RatePhotoService } from './rate-photo.service';

@Component({
  templateUrl: './rate-photo-delete-dialog.component.html'
})
export class RatePhotoDeleteDialogComponent {
  rate: IRatePhoto;

  constructor(protected rateService: RatePhotoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.rateService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'rateListModification',
        content: 'Deleted an rate'
      });
      this.activeModal.dismiss(true);
    });
  }
}
