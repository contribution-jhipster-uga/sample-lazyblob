import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { RatePhoto } from 'app/shared/model/rate-photo.model';
import { RatePhotoService } from './rate-photo.service';
import { RatePhotoComponent } from './rate-photo.component';
import { RatePhotoDetailComponent } from './rate-photo-detail.component';
import { RatePhotoUpdateComponent } from './rate-photo-update.component';
import { IRatePhoto } from 'app/shared/model/rate-photo.model';

@Injectable({ providedIn: 'root' })
export class RatePhotoResolve implements Resolve<IRatePhoto> {
  constructor(private service: RatePhotoService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRatePhoto> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((rate: HttpResponse<RatePhoto>) => rate.body));
    }
    return of(new RatePhoto());
  }
}

export const rateRoute: Routes = [
  {
    path: '',
    component: RatePhotoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'lazyblobApp.rate.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: RatePhotoDetailComponent,
    resolve: {
      rate: RatePhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'lazyblobApp.rate.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: RatePhotoUpdateComponent,
    resolve: {
      rate: RatePhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'lazyblobApp.rate.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RatePhotoUpdateComponent,
    resolve: {
      rate: RatePhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'lazyblobApp.rate.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
