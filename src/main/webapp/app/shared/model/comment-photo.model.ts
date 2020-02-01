import { Moment } from 'moment';

export interface ICommentPhoto {
  id?: number;
  text?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  fromLogin?: string;
  fromId?: number;
  photoTitle?: string;
  photoId?: number;
}

export class CommentPhoto implements ICommentPhoto {
  constructor(
    public id?: number,
    public text?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public fromLogin?: string,
    public fromId?: number,
    public photoTitle?: string,
    public photoId?: number
  ) {}
}
