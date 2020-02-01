import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAlbumPhoto } from 'app/shared/model/album-photo.model';

type EntityResponseType = HttpResponse<IAlbumPhoto>;
type EntityArrayResponseType = HttpResponse<IAlbumPhoto[]>;

@Injectable({ providedIn: 'root' })
export class AlbumPhotoService {
  public resourceUrl = SERVER_API_URL + 'api/albums';

  constructor(protected http: HttpClient) {}

  create(album: IAlbumPhoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(album);
    return this.http
      .post<IAlbumPhoto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(album: IAlbumPhoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(album);
    return this.http
      .put<IAlbumPhoto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAlbumPhoto>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAlbumPhoto[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(album: IAlbumPhoto): IAlbumPhoto {
    const copy: IAlbumPhoto = Object.assign({}, album, {
      createdAt: album.createdAt != null && album.createdAt.isValid() ? album.createdAt.toJSON() : null,
      updatedAt: album.updatedAt != null && album.updatedAt.isValid() ? album.updatedAt.toJSON() : null
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
      res.body.forEach((album: IAlbumPhoto) => {
        album.createdAt = album.createdAt != null ? moment(album.createdAt) : null;
        album.updatedAt = album.updatedAt != null ? moment(album.updatedAt) : null;
      });
    }
    return res;
  }
}
