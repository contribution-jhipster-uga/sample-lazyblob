import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LazyblobTestModule } from '../../../test.module';
import { CommentPhotoUpdateComponent } from 'app/entities/comment-photo/comment-photo-update.component';
import { CommentPhotoService } from 'app/entities/comment-photo/comment-photo.service';
import { CommentPhoto } from 'app/shared/model/comment-photo.model';

describe('Component Tests', () => {
  describe('CommentPhoto Management Update Component', () => {
    let comp: CommentPhotoUpdateComponent;
    let fixture: ComponentFixture<CommentPhotoUpdateComponent>;
    let service: CommentPhotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LazyblobTestModule],
        declarations: [CommentPhotoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CommentPhotoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommentPhotoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CommentPhotoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CommentPhoto(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new CommentPhoto();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
