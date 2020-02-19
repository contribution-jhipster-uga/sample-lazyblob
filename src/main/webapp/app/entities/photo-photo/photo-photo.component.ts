import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPhotoPhoto } from 'app/shared/model/photo-photo.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PhotoPhotoService } from './photo-photo.service';
import { PhotoPhotoDeleteDialogComponent } from './photo-photo-delete-dialog.component';

@Component({
  selector: 'jhi-photo-photo',
  templateUrl: './photo-photo.component.html'
})
export class PhotoPhotoComponent implements OnInit, OnDestroy {
  photos: IPhotoPhoto[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;

  constructor(
    protected photoService: PhotoPhotoService,
    protected parseLinks: JhiParseLinks,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: JhiDataUtils,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  loadAll() {
    this.photoService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPhotoPhoto[]>) => this.paginatePhotos(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/photo-photo'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/photo-photo',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInPhotos();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPhotoPhoto) {
    return item.id;
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  registerChangeInPhotos() {
    this.eventSubscriber = this.eventManager.subscribe('photoListModification', () => this.loadAll());
  }

  delete(photo: IPhotoPhoto) {
    const modalRef = this.modalService.open(PhotoPhotoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.photo = photo;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePhotos(data: IPhotoPhoto[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.photos = data;
    /* this.photos.forEach(p => {
      this.photoService.http
        .get('/api/photos/' + p.id + '/image', { responseType: 'arraybuffer' })
        .subscribe(res => (p.image = window.URL.createObjectURL(new Blob([res], { type: 'image/png' }))));
      this.photoService.http
        .get('/api/photos/' + p.id + '/thumbnailx1', { responseType: 'arraybuffer' })
        .subscribe(res => (p.thumbnailx1 = window.URL.createObjectURL(new Blob([res], { type: 'image/png' }))));
      this.photoService.http
        .get('/api/photos/' + p.id + '/thumbnailx2', { responseType: 'arraybuffer' })
        .subscribe(res => (p.thumbnailx2 = window.URL.createObjectURL(new Blob([res], { type: 'image/png' }))));
      console.log(p.image);
    });*/
  }
}
