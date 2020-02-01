import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommentPhoto } from 'app/shared/model/comment-photo.model';

@Component({
  selector: 'jhi-comment-photo-detail',
  templateUrl: './comment-photo-detail.component.html'
})
export class CommentPhotoDetailComponent implements OnInit {
  comment: ICommentPhoto;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ comment }) => {
      this.comment = comment;
    });
  }

  previousState() {
    window.history.back();
  }
}
