import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRatePhoto } from 'app/shared/model/rate-photo.model';

type EntityResponseType = HttpResponse<IRatePhoto>;
type EntityArrayResponseType = HttpResponse<IRatePhoto[]>;

@Injectable({ providedIn: 'root' })
export class RatePhotoService {
  public resourceUrl = SERVER_API_URL + 'api/rates';

  constructor(protected http: HttpClient) {}

  create(rate: IRatePhoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rate);
    return this.http
      .post<IRatePhoto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rate: IRatePhoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rate);
    return this.http
      .put<IRatePhoto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRatePhoto>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRatePhoto[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(rate: IRatePhoto): IRatePhoto {
    const copy: IRatePhoto = Object.assign({}, rate, {
      createdAt: rate.createdAt != null && rate.createdAt.isValid() ? rate.createdAt.toJSON() : null,
      updatedAt: rate.updatedAt != null && rate.updatedAt.isValid() ? rate.updatedAt.toJSON() : null
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
      res.body.forEach((rate: IRatePhoto) => {
        rate.createdAt = rate.createdAt != null ? moment(rate.createdAt) : null;
        rate.updatedAt = rate.updatedAt != null ? moment(rate.updatedAt) : null;
      });
    }
    return res;
  }
}
