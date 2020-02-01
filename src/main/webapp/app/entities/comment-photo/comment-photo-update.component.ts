import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ICommentPhoto, CommentPhoto } from 'app/shared/model/comment-photo.model';
import { CommentPhotoService } from './comment-photo.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IPhotoPhoto } from 'app/shared/model/photo-photo.model';
import { PhotoPhotoService } from 'app/entities/photo-photo/photo-photo.service';

@Component({
  selector: 'jhi-comment-photo-update',
  templateUrl: './comment-photo-update.component.html'
})
export class CommentPhotoUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  photos: IPhotoPhoto[];

  editForm = this.fb.group({
    id: [],
    text: [null, [Validators.required, Validators.minLength(5)]],
    createdAt: [null, [Validators.required]],
    updatedAt: [],
    fromId: [],
    photoId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected commentService: CommentPhotoService,
    protected userService: UserService,
    protected photoService: PhotoPhotoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ comment }) => {
      this.updateForm(comment);
    });
    this.userService
      .query()
      .subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.photoService
      .query()
      .subscribe((res: HttpResponse<IPhotoPhoto[]>) => (this.photos = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(comment: ICommentPhoto) {
    this.editForm.patchValue({
      id: comment.id,
      text: comment.text,
      createdAt: comment.createdAt != null ? comment.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: comment.updatedAt != null ? comment.updatedAt.format(DATE_TIME_FORMAT) : null,
      fromId: comment.fromId,
      photoId: comment.photoId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const comment = this.createFromForm();
    if (comment.id !== undefined) {
      this.subscribeToSaveResponse(this.commentService.update(comment));
    } else {
      this.subscribeToSaveResponse(this.commentService.create(comment));
    }
  }

  private createFromForm(): ICommentPhoto {
    return {
      ...new CommentPhoto(),
      id: this.editForm.get(['id']).value,
      text: this.editForm.get(['text']).value,
      createdAt:
        this.editForm.get(['createdAt']).value != null ? moment(this.editForm.get(['createdAt']).value, DATE_TIME_FORMAT) : undefined,
      updatedAt:
        this.editForm.get(['updatedAt']).value != null ? moment(this.editForm.get(['updatedAt']).value, DATE_TIME_FORMAT) : undefined,
      fromId: this.editForm.get(['fromId']).value,
      photoId: this.editForm.get(['photoId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommentPhoto>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackPhotoById(index: number, item: IPhotoPhoto) {
    return item.id;
  }
}
