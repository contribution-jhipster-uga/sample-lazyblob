import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LazyblobTestModule } from '../../../test.module';
import { PhotoPhotoUpdateComponent } from 'app/entities/photo-photo/photo-photo-update.component';
import { PhotoPhotoService } from 'app/entities/photo-photo/photo-photo.service';
import { PhotoPhoto } from 'app/shared/model/photo-photo.model';

describe('Component Tests', () => {
  describe('PhotoPhoto Management Update Component', () => {
    let comp: PhotoPhotoUpdateComponent;
    let fixture: ComponentFixture<PhotoPhotoUpdateComponent>;
    let service: PhotoPhotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LazyblobTestModule],
        declarations: [PhotoPhotoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PhotoPhotoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PhotoPhotoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PhotoPhotoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PhotoPhoto(123);
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
        const entity = new PhotoPhoto();
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
