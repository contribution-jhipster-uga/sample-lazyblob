import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LazyblobTestModule } from '../../../test.module';
import { AlbumPhotoUpdateComponent } from 'app/entities/album-photo/album-photo-update.component';
import { AlbumPhotoService } from 'app/entities/album-photo/album-photo.service';
import { AlbumPhoto } from 'app/shared/model/album-photo.model';

describe('Component Tests', () => {
  describe('AlbumPhoto Management Update Component', () => {
    let comp: AlbumPhotoUpdateComponent;
    let fixture: ComponentFixture<AlbumPhotoUpdateComponent>;
    let service: AlbumPhotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LazyblobTestModule],
        declarations: [AlbumPhotoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AlbumPhotoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AlbumPhotoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AlbumPhotoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AlbumPhoto(123);
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
        const entity = new AlbumPhoto();
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
