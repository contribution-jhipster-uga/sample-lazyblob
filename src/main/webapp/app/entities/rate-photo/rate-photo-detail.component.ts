import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRatePhoto } from 'app/shared/model/rate-photo.model';

@Component({
  selector: 'jhi-rate-photo-detail',
  templateUrl: './rate-photo-detail.component.html'
})
export class RatePhotoDetailComponent implements OnInit {
  rate: IRatePhoto;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ rate }) => {
      this.rate = rate;
    });
  }

  previousState() {
    window.history.back();
  }
}
