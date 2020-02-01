import { element, by, ElementFinder } from 'protractor';

export class RateComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-rate-photo div table .btn-danger'));
  title = element.all(by.css('jhi-rate-photo div h2#page-heading span')).first();

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

export class RateUpdatePage {
  pageTitle = element(by.id('jhi-rate-photo-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  rateInput = element(by.id('field_rate'));
  createdAtInput = element(by.id('field_createdAt'));
  updatedAtInput = element(by.id('field_updatedAt'));
  photoSelect = element(by.id('field_photo'));
  fromSelect = element(by.id('field_from'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setRateInput(rate) {
    await this.rateInput.sendKeys(rate);
  }

  async getRateInput() {
    return await this.rateInput.getAttribute('value');
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

export class RateDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-rate-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-rate'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}
