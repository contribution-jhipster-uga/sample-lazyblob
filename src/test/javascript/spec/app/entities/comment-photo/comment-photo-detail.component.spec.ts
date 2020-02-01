import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LazyblobTestModule } from '../../../test.module';
import { CommentPhotoDetailComponent } from 'app/entities/comment-photo/comment-photo-detail.component';
import { CommentPhoto } from 'app/shared/model/comment-photo.model';

describe('Component Tests', () => {
  describe('CommentPhoto Management Detail Component', () => {
    let comp: CommentPhotoDetailComponent;
    let fixture: ComponentFixture<CommentPhotoDetailComponent>;
    const route = ({ data: of({ comment: new CommentPhoto(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LazyblobTestModule],
        declarations: [CommentPhotoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CommentPhotoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CommentPhotoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.comment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
