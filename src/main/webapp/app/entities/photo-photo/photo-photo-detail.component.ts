import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IPhotoPhoto } from 'app/shared/model/photo-photo.model';

@Component({
  selector: 'jhi-photo-photo-detail',
  templateUrl: './photo-photo-detail.component.html'
})
export class PhotoPhotoDetailComponent implements OnInit {
  photo: IPhotoPhoto;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ photo }) => {
      this.photo = photo;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}
