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
import { IAlbumPhoto, AlbumPhoto } from 'app/shared/model/album-photo.model';
import { AlbumPhotoService } from './album-photo.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-album-photo-update',
  templateUrl: './album-photo-update.component.html'
})
export class AlbumPhotoUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    title: [],
    note: [],
    createdAt: [null, [Validators.required]],
    updatedAt: [],
    ownedById: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected albumService: AlbumPhotoService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ album }) => {
      this.updateForm(album);
    });
    this.userService
      .query()
      .subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(album: IAlbumPhoto) {
    this.editForm.patchValue({
      id: album.id,
      title: album.title,
      note: album.note,
      createdAt: album.createdAt != null ? album.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: album.updatedAt != null ? album.updatedAt.format(DATE_TIME_FORMAT) : null,
      ownedById: album.ownedById
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const album = this.createFromForm();
    if (album.id !== undefined) {
      this.subscribeToSaveResponse(this.albumService.update(album));
    } else {
      this.subscribeToSaveResponse(this.albumService.create(album));
    }
  }

  private createFromForm(): IAlbumPhoto {
    return {
      ...new AlbumPhoto(),
      id: this.editForm.get(['id']).value,
      title: this.editForm.get(['title']).value,
      note: this.editForm.get(['note']).value,
      createdAt:
        this.editForm.get(['createdAt']).value != null ? moment(this.editForm.get(['createdAt']).value, DATE_TIME_FORMAT) : undefined,
      updatedAt:
        this.editForm.get(['updatedAt']).value != null ? moment(this.editForm.get(['updatedAt']).value, DATE_TIME_FORMAT) : undefined,
      ownedById: this.editForm.get(['ownedById']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlbumPhoto>>) {
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
}
