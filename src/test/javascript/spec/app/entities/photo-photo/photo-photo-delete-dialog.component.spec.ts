import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LazyblobTestModule } from '../../../test.module';
import { PhotoPhotoDeleteDialogComponent } from 'app/entities/photo-photo/photo-photo-delete-dialog.component';
import { PhotoPhotoService } from 'app/entities/photo-photo/photo-photo.service';

describe('Component Tests', () => {
  describe('PhotoPhoto Management Delete Component', () => {
    let comp: PhotoPhotoDeleteDialogComponent;
    let fixture: ComponentFixture<PhotoPhotoDeleteDialogComponent>;
    let service: PhotoPhotoService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LazyblobTestModule],
        declarations: [PhotoPhotoDeleteDialogComponent]
      })
        .overrideTemplate(PhotoPhotoDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PhotoPhotoDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PhotoPhotoService);
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
