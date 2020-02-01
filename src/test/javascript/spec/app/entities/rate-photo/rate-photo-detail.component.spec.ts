import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LazyblobTestModule } from '../../../test.module';
import { RatePhotoDetailComponent } from 'app/entities/rate-photo/rate-photo-detail.component';
import { RatePhoto } from 'app/shared/model/rate-photo.model';

describe('Component Tests', () => {
  describe('RatePhoto Management Detail Component', () => {
    let comp: RatePhotoDetailComponent;
    let fixture: ComponentFixture<RatePhotoDetailComponent>;
    const route = ({ data: of({ rate: new RatePhoto(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LazyblobTestModule],
        declarations: [RatePhotoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RatePhotoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RatePhotoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.rate).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
