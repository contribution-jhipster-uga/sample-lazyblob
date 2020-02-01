import { Moment } from 'moment';

export interface IAlbumPhoto {
  id?: number;
  title?: string;
  note?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  ownedByLogin?: string;
  ownedById?: number;
}

export class AlbumPhoto implements IAlbumPhoto {
  constructor(
    public id?: number,
    public title?: string,
    public note?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public ownedByLogin?: string,
    public ownedById?: number
  ) {}
}
