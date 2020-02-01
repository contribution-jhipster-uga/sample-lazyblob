import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICommentPhoto } from 'app/shared/model/comment-photo.model';

type EntityResponseType = HttpResponse<ICommentPhoto>;
type EntityArrayResponseType = HttpResponse<ICommentPhoto[]>;

@Injectable({ providedIn: 'root' })
export class CommentPhotoService {
  public resourceUrl = SERVER_API_URL + 'api/comments';

  constructor(protected http: HttpClient) {}

  create(comment: ICommentPhoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comment);
    return this.http
      .post<ICommentPhoto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(comment: ICommentPhoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comment);
    return this.http
      .put<ICommentPhoto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICommentPhoto>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICommentPhoto[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(comment: ICommentPhoto): ICommentPhoto {
    const copy: ICommentPhoto = Object.assign({}, comment, {
      createdAt: comment.createdAt != null && comment.createdAt.isValid() ? comment.createdAt.toJSON() : null,
      updatedAt: comment.updatedAt != null && comment.updatedAt.isValid() ? comment.updatedAt.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt != null ? moment(res.body.createdAt) : null;
      res.body.updatedAt = res.body.updatedAt != null ? moment(res.body.updatedAt) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((comment: ICommentPhoto) => {
        comment.createdAt = comment.createdAt != null ? moment(comment.createdAt) : null;
        comment.updatedAt = comment.updatedAt != null ? moment(comment.updatedAt) : null;
      });
    }
    return res;
  }
}
