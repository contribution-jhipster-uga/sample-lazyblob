import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AlbumPhoto } from 'app/shared/model/album-photo.model';
import { AlbumPhotoService } from './album-photo.service';
import { AlbumPhotoComponent } from './album-photo.component';
import { AlbumPhotoDetailComponent } from './album-photo-detail.component';
import { AlbumPhotoUpdateComponent } from './album-photo-update.component';
import { IAlbumPhoto } from 'app/shared/model/album-photo.model';

@Injectable({ providedIn: 'root' })
export class AlbumPhotoResolve implements Resolve<IAlbumPhoto> {
  constructor(private service: AlbumPhotoService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAlbumPhoto> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((album: HttpResponse<AlbumPhoto>) => album.body));
    }
    return of(new AlbumPhoto());
  }
}

export const albumRoute: Routes = [
  {
    path: '',
    component: AlbumPhotoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'lazyblobApp.album.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AlbumPhotoDetailComponent,
    resolve: {
      album: AlbumPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'lazyblobApp.album.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AlbumPhotoUpdateComponent,
    resolve: {
      album: AlbumPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'lazyblobApp.album.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AlbumPhotoUpdateComponent,
    resolve: {
      album: AlbumPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'lazyblobApp.album.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
