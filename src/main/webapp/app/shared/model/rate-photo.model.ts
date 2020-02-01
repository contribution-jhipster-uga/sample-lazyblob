import { Moment } from 'moment';

export interface IRatePhoto {
  id?: number;
  rate?: number;
  createdAt?: Moment;
  updatedAt?: Moment;
  photoTitle?: string;
  photoId?: number;
  fromLogin?: string;
  fromId?: number;
}

export class RatePhoto implements IRatePhoto {
  constructor(
    public id?: number,
    public rate?: number,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public photoTitle?: string,
    public photoId?: number,
    public fromLogin?: string,
    public fromId?: number
  ) {}
}
