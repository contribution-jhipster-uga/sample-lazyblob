import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CommentPhoto } from 'app/shared/model/comment-photo.model';
import { CommentPhotoService } from './comment-photo.service';
import { CommentPhotoComponent } from './comment-photo.component';
import { CommentPhotoDetailComponent } from './comment-photo-detail.component';
import { CommentPhotoUpdateComponent } from './comment-photo-update.component';
import { ICommentPhoto } from 'app/shared/model/comment-photo.model';

@Injectable({ providedIn: 'root' })
export class CommentPhotoResolve implements Resolve<ICommentPhoto> {
  constructor(private service: CommentPhotoService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommentPhoto> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((comment: HttpResponse<CommentPhoto>) => comment.body));
    }
    return of(new CommentPhoto());
  }
}

export const commentRoute: Routes = [
  {
    path: '',
    component: CommentPhotoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'lazyblobApp.comment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CommentPhotoDetailComponent,
    resolve: {
      comment: CommentPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'lazyblobApp.comment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CommentPhotoUpdateComponent,
    resolve: {
      comment: CommentPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'lazyblobApp.comment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CommentPhotoUpdateComponent,
    resolve: {
      comment: CommentPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'lazyblobApp.comment.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
