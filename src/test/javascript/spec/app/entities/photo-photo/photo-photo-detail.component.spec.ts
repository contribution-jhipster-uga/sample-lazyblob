import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LazyblobTestModule } from '../../../test.module';
import { PhotoPhotoDetailComponent } from 'app/entities/photo-photo/photo-photo-detail.component';
import { PhotoPhoto } from 'app/shared/model/photo-photo.model';

describe('Component Tests', () => {
  describe('PhotoPhoto Management Detail Component', () => {
    let comp: PhotoPhotoDetailComponent;
    let fixture: ComponentFixture<PhotoPhotoDetailComponent>;
    const route = ({ data: of({ photo: new PhotoPhoto(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LazyblobTestModule],
        declarations: [PhotoPhotoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PhotoPhotoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PhotoPhotoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.photo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
