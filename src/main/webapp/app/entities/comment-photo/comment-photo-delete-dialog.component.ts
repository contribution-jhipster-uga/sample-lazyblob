import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICommentPhoto } from 'app/shared/model/comment-photo.model';
import { CommentPhotoService } from './comment-photo.service';

@Component({
  templateUrl: './comment-photo-delete-dialog.component.html'
})
export class CommentPhotoDeleteDialogComponent {
  comment: ICommentPhoto;

  constructor(protected commentService: CommentPhotoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.commentService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'commentListModification',
        content: 'Deleted an comment'
      });
      this.activeModal.dismiss(true);
    });
  }
}
