import { element, by, ElementFinder } from 'protractor';

export class PhotoComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-photo-photo div table .btn-danger'));
  title = element.all(by.css('jhi-photo-photo div h2#page-heading span')).first();

  async clickOnCreateButton() {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton() {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class PhotoUpdatePage {
  pageTitle = element(by.id('jhi-photo-photo-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  titleInput = element(by.id('field_title'));
  noteInput = element(by.id('field_note'));
  imageInput = element(by.id('file_image'));
  imageSha1Input = element(by.id('field_imageSha1'));
  thumbnailx1Input = element(by.id('file_thumbnailx1'));
  thumbnailx1Sha1Input = element(by.id('field_thumbnailx1Sha1'));
  thumbnailx2Input = element(by.id('file_thumbnailx2'));
  thumbnailx2Sha1Input = element(by.id('field_thumbnailx2Sha1'));
  exifInput = element(by.id('field_exif'));
  extractedTextInput = element(by.id('field_extractedText'));
  detectedObjectsInput = element(by.id('field_detectedObjects'));
  createdAtInput = element(by.id('field_createdAt'));
  updatedAtInput = element(by.id('field_updatedAt'));
  belongToSelect = element(by.id('field_belongTo'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setTitleInput(title) {
    await this.titleInput.sendKeys(title);
  }

  async getTitleInput() {
    return await this.titleInput.getAttribute('value');
  }

  async setNoteInput(note) {
    await this.noteInput.sendKeys(note);
  }

  async getNoteInput() {
    return await this.noteInput.getAttribute('value');
  }

  async setImageInput(image) {
    await this.imageInput.sendKeys(image);
  }

  async getImageInput() {
    return await this.imageInput.getAttribute('value');
  }

  async setImageSha1Input(imageSha1) {
    await this.imageSha1Input.sendKeys(imageSha1);
  }

  async getImageSha1Input() {
    return await this.imageSha1Input.getAttribute('value');
  }

  async setThumbnailx1Input(thumbnailx1) {
    await this.thumbnailx1Input.sendKeys(thumbnailx1);
  }

  async getThumbnailx1Input() {
    return await this.thumbnailx1Input.getAttribute('value');
  }

  async setThumbnailx1Sha1Input(thumbnailx1Sha1) {
    await this.thumbnailx1Sha1Input.sendKeys(thumbnailx1Sha1);
  }

  async getThumbnailx1Sha1Input() {
    return await this.thumbnailx1Sha1Input.getAttribute('value');
  }

  async setThumbnailx2Input(thumbnailx2) {
    await this.thumbnailx2Input.sendKeys(thumbnailx2);
  }

  async getThumbnailx2Input() {
    return await this.thumbnailx2Input.getAttribute('value');
  }

  async setThumbnailx2Sha1Input(thumbnailx2Sha1) {
    await this.thumbnailx2Sha1Input.sendKeys(thumbnailx2Sha1);
  }

  async getThumbnailx2Sha1Input() {
    return await this.thumbnailx2Sha1Input.getAttribute('value');
  }

  async setExifInput(exif) {
    await this.exifInput.sendKeys(exif);
  }

  async getExifInput() {
    return await this.exifInput.getAttribute('value');
  }

  async setExtractedTextInput(extractedText) {
    await this.extractedTextInput.sendKeys(extractedText);
  }

  async getExtractedTextInput() {
    return await this.extractedTextInput.getAttribute('value');
  }

  async setDetectedObjectsInput(detectedObjects) {
    await this.detectedObjectsInput.sendKeys(detectedObjects);
  }

  async getDetectedObjectsInput() {
    return await this.detectedObjectsInput.getAttribute('value');
  }

  async setCreatedAtInput(createdAt) {
    await this.createdAtInput.sendKeys(createdAt);
  }

  async getCreatedAtInput() {
    return await this.createdAtInput.getAttribute('value');
  }

  async setUpdatedAtInput(updatedAt) {
    await this.updatedAtInput.sendKeys(updatedAt);
  }

  async getUpdatedAtInput() {
    return await this.updatedAtInput.getAttribute('value');
  }

  async belongToSelectLastOption() {
    await this.belongToSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async belongToSelectOption(option) {
    await this.belongToSelect.sendKeys(option);
  }

  getBelongToSelect(): ElementFinder {
    return this.belongToSelect;
  }

  async getBelongToSelectedOption() {
    return await this.belongToSelect.element(by.css('option:checked')).getText();
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class PhotoDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-photo-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-photo'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}
