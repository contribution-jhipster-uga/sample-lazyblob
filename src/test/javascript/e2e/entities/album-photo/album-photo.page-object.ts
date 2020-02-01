import { element, by, ElementFinder } from 'protractor';

export class AlbumComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-album-photo div table .btn-danger'));
  title = element.all(by.css('jhi-album-photo div h2#page-heading span')).first();

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

export class AlbumUpdatePage {
  pageTitle = element(by.id('jhi-album-photo-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  titleInput = element(by.id('field_title'));
  noteInput = element(by.id('field_note'));
  createdAtInput = element(by.id('field_createdAt'));
  updatedAtInput = element(by.id('field_updatedAt'));
  ownedBySelect = element(by.id('field_ownedBy'));

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

  async ownedBySelectLastOption() {
    await this.ownedBySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async ownedBySelectOption(option) {
    await this.ownedBySelect.sendKeys(option);
  }

  getOwnedBySelect(): ElementFinder {
    return this.ownedBySelect;
  }

  async getOwnedBySelectedOption() {
    return await this.ownedBySelect.element(by.css('option:checked')).getText();
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

export class AlbumDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-album-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-album'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}
