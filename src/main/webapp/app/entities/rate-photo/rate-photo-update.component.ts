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
import { IRatePhoto, RatePhoto } from 'app/shared/model/rate-photo.model';
import { RatePhotoService } from './rate-photo.service';
import { IPhotoPhoto } from 'app/shared/model/photo-photo.model';
import { PhotoPhotoService } from 'app/entities/photo-photo/photo-photo.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-rate-photo-update',
  templateUrl: './rate-photo-update.component.html'
})
export class RatePhotoUpdateComponent implements OnInit {
  isSaving: boolean;

  photos: IPhotoPhoto[];

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    rate: [null, [Validators.min(0), Validators.max(5)]],
    createdAt: [null, [Validators.required]],
    updatedAt: [],
    photoId: [],
    fromId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected rateService: RatePhotoService,
    protected photoService: PhotoPhotoService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ rate }) => {
      this.updateForm(rate);
    });
    this.photoService
      .query()
      .subscribe((res: HttpResponse<IPhotoPhoto[]>) => (this.photos = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.userService
      .query()
      .subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(rate: IRatePhoto) {
    this.editForm.patchValue({
      id: rate.id,
      rate: rate.rate,
      createdAt: rate.createdAt != null ? rate.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: rate.updatedAt != null ? rate.updatedAt.format(DATE_TIME_FORMAT) : null,
      photoId: rate.photoId,
      fromId: rate.fromId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const rate = this.createFromForm();
    if (rate.id !== undefined) {
      this.subscribeToSaveResponse(this.rateService.update(rate));
    } else {
      this.subscribeToSaveResponse(this.rateService.create(rate));
    }
  }

  private createFromForm(): IRatePhoto {
    return {
      ...new RatePhoto(),
      id: this.editForm.get(['id']).value,
      rate: this.editForm.get(['rate']).value,
      createdAt:
        this.editForm.get(['createdAt']).value != null ? moment(this.editForm.get(['createdAt']).value, DATE_TIME_FORMAT) : undefined,
      updatedAt:
        this.editForm.get(['updatedAt']).value != null ? moment(this.editForm.get(['updatedAt']).value, DATE_TIME_FORMAT) : undefined,
      photoId: this.editForm.get(['photoId']).value,
      fromId: this.editForm.get(['fromId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRatePhoto>>) {
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

  trackPhotoById(index: number, item: IPhotoPhoto) {
    return item.id;
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
