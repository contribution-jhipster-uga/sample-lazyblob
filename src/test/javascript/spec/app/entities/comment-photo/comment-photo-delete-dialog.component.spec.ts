import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LazyblobTestModule } from '../../../test.module';
import { CommentPhotoDeleteDialogComponent } from 'app/entities/comment-photo/comment-photo-delete-dialog.component';
import { CommentPhotoService } from 'app/entities/comment-photo/comment-photo.service';

describe('Component Tests', () => {
  describe('CommentPhoto Management Delete Component', () => {
    let comp: CommentPhotoDeleteDialogComponent;
    let fixture: ComponentFixture<CommentPhotoDeleteDialogComponent>;
    let service: CommentPhotoService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LazyblobTestModule],
        declarations: [CommentPhotoDeleteDialogComponent]
      })
        .overrideTemplate(CommentPhotoDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CommentPhotoDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CommentPhotoService);
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
