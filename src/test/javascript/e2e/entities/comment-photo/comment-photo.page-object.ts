import { element, by, ElementFinder } from 'protractor';

export class CommentComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-comment-photo div table .btn-danger'));
  title = element.all(by.css('jhi-comment-photo div h2#page-heading span')).first();

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

export class CommentUpdatePage {
  pageTitle = element(by.id('jhi-comment-photo-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  textInput = element(by.id('field_text'));
  createdAtInput = element(by.id('field_createdAt'));
  updatedAtInput = element(by.id('field_updatedAt'));
  fromSelect = element(by.id('field_from'));
  photoSelect = element(by.id('field_photo'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setTextInput(text) {
    await this.textInput.sendKeys(text);
  }

  async getTextInput() {
    return await this.textInput.getAttribute('value');
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

  async fromSelectLastOption() {
    await this.fromSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async fromSelectOption(option) {
    await this.fromSelect.sendKeys(option);
  }

  getFromSelect(): ElementFinder {
    return this.fromSelect;
  }

  async getFromSelectedOption() {
    return await this.fromSelect.element(by.css('option:checked')).getText();
  }

  async photoSelectLastOption() {
    await this.photoSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async photoSelectOption(option) {
    await this.photoSelect.sendKeys(option);
  }

  getPhotoSelect(): ElementFinder {
    return this.photoSelect;
  }

  async getPhotoSelectedOption() {
    return await this.photoSelect.element(by.css('option:checked')).getText();
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

export class CommentDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-comment-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-comment'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}
