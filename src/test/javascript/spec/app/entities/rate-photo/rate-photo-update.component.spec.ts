import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LazyblobTestModule } from '../../../test.module';
import { RatePhotoUpdateComponent } from 'app/entities/rate-photo/rate-photo-update.component';
import { RatePhotoService } from 'app/entities/rate-photo/rate-photo.service';
import { RatePhoto } from 'app/shared/model/rate-photo.model';

describe('Component Tests', () => {
  describe('RatePhoto Management Update Component', () => {
    let comp: RatePhotoUpdateComponent;
    let fixture: ComponentFixture<RatePhotoUpdateComponent>;
    let service: RatePhotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LazyblobTestModule],
        declarations: [RatePhotoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(RatePhotoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RatePhotoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RatePhotoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new RatePhoto(123);
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
        const entity = new RatePhoto();
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
