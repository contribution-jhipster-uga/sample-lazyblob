import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LazyblobTestModule } from '../../../test.module';
import { AlbumPhotoDetailComponent } from 'app/entities/album-photo/album-photo-detail.component';
import { AlbumPhoto } from 'app/shared/model/album-photo.model';

describe('Component Tests', () => {
  describe('AlbumPhoto Management Detail Component', () => {
    let comp: AlbumPhotoDetailComponent;
    let fixture: ComponentFixture<AlbumPhotoDetailComponent>;
    const route = ({ data: of({ album: new AlbumPhoto(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LazyblobTestModule],
        declarations: [AlbumPhotoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AlbumPhotoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AlbumPhotoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.album).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
