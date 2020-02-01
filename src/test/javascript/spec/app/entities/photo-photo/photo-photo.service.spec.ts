import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { PhotoPhotoService } from 'app/entities/photo-photo/photo-photo.service';
import { IPhotoPhoto, PhotoPhoto } from 'app/shared/model/photo-photo.model';

describe('Service Tests', () => {
  describe('PhotoPhoto Service', () => {
    let injector: TestBed;
    let service: PhotoPhotoService;
    let httpMock: HttpTestingController;
    let elemDefault: IPhotoPhoto;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(PhotoPhotoService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new PhotoPhoto(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'image/png',
        'AAAAAAA',
        'AAAAAAA',
        'image/png',
        'AAAAAAA',
        'AAAAAAA',
        'image/png',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a PhotoPhoto', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate
          },
          returnedFromService
        );
        service
          .create(new PhotoPhoto(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a PhotoPhoto', () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            note: 'BBBBBB',
            image: 'BBBBBB',
            imageSha1: 'BBBBBB',
            thumbnailx1: 'BBBBBB',
            thumbnailx1Sha1: 'BBBBBB',
            thumbnailx2: 'BBBBBB',
            thumbnailx2Sha1: 'BBBBBB',
            exif: 'BBBBBB',
            extractedText: 'BBBBBB',
            detectedObjects: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of PhotoPhoto', () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            note: 'BBBBBB',
            image: 'BBBBBB',
            imageSha1: 'BBBBBB',
            thumbnailx1: 'BBBBBB',
            thumbnailx1Sha1: 'BBBBBB',
            thumbnailx2: 'BBBBBB',
            thumbnailx2Sha1: 'BBBBBB',
            exif: 'BBBBBB',
            extractedText: 'BBBBBB',
            detectedObjects: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PhotoPhoto', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
