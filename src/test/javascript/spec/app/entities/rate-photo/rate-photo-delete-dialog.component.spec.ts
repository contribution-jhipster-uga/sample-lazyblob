import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LazyblobTestModule } from '../../../test.module';
import { RatePhotoDeleteDialogComponent } from 'app/entities/rate-photo/rate-photo-delete-dialog.component';
import { RatePhotoService } from 'app/entities/rate-photo/rate-photo.service';

describe('Component Tests', () => {
  describe('RatePhoto Management Delete Component', () => {
    let comp: RatePhotoDeleteDialogComponent;
    let fixture: ComponentFixture<RatePhotoDeleteDialogComponent>;
    let service: RatePhotoService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LazyblobTestModule],
        declarations: [RatePhotoDeleteDialogComponent]
      })
        .overrideTemplate(RatePhotoDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RatePhotoDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RatePhotoService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
