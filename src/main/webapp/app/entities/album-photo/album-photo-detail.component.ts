import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAlbumPhoto } from 'app/shared/model/album-photo.model';

@Component({
  selector: 'jhi-album-photo-detail',
  templateUrl: './album-photo-detail.component.html'
})
export class AlbumPhotoDetailComponent implements OnInit {
  album: IAlbumPhoto;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ album }) => {
      this.album = album;
    });
  }

  previousState() {
    window.history.back();
  }
}
