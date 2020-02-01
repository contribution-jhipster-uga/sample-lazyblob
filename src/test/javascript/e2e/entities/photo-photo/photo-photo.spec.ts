import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PhotoComponentsPage, PhotoDeleteDialog, PhotoUpdatePage } from './photo-photo.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Photo e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let photoComponentsPage: PhotoComponentsPage;
  let photoUpdatePage: PhotoUpdatePage;
  let photoDeleteDialog: PhotoDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Photos', async () => {
    await navBarPage.goToEntity('photo-photo');
    photoComponentsPage = new PhotoComponentsPage();
    await browser.wait(ec.visibilityOf(photoComponentsPage.title), 5000);
    expect(await photoComponentsPage.getTitle()).to.eq('lazyblobApp.photo.home.title');
  });

  it('should load create Photo page', async () => {
    await photoComponentsPage.clickOnCreateButton();
    photoUpdatePage = new PhotoUpdatePage();
    expect(await photoUpdatePage.getPageTitle()).to.eq('lazyblobApp.photo.home.createOrEditLabel');
    await photoUpdatePage.cancel();
  });

  it('should create and save Photos', async () => {
    const nbButtonsBeforeCreate = await photoComponentsPage.countDeleteButtons();

    await photoComponentsPage.clickOnCreateButton();
    await promise.all([
      photoUpdatePage.setTitleInput('title'),
      photoUpdatePage.setNoteInput('note'),
      photoUpdatePage.setImageInput(absolutePath),
      photoUpdatePage.setImageSha1Input('dc80deF2C1bdd6CeF041b2c61bEF7a6bc560E812'),
      photoUpdatePage.setThumbnailx1Input(absolutePath),
      photoUpdatePage.setThumbnailx1Sha1Input('fC3eDBCa0C1aC6e158aBEb04ea69b2aa94dbc079'),
      photoUpdatePage.setThumbnailx2Input(absolutePath),
      photoUpdatePage.setThumbnailx2Sha1Input('0655fDb7D0fd053Fd3A6AbBE1ddfA660CDaC8Cd7'),
      photoUpdatePage.setExifInput('exif'),
      photoUpdatePage.setExtractedTextInput('extractedText'),
      photoUpdatePage.setDetectedObjectsInput('detectedObjects'),
      photoUpdatePage.setCreatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      photoUpdatePage.setUpdatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      photoUpdatePage.belongToSelectLastOption()
    ]);
    expect(await photoUpdatePage.getTitleInput()).to.eq('title', 'Expected Title value to be equals to title');
    expect(await photoUpdatePage.getNoteInput()).to.eq('note', 'Expected Note value to be equals to note');
    expect(await photoUpdatePage.getImageInput()).to.endsWith(fileNameToUpload, 'Expected Image value to be end with ' + fileNameToUpload);
    expect(await photoUpdatePage.getImageSha1Input()).to.eq(
      'dc80deF2C1bdd6CeF041b2c61bEF7a6bc560E812',
      'Expected ImageSha1 value to be equals to dc80deF2C1bdd6CeF041b2c61bEF7a6bc560E812'
    );
    expect(await photoUpdatePage.getThumbnailx1Input()).to.endsWith(
      fileNameToUpload,
      'Expected Thumbnailx1 value to be end with ' + fileNameToUpload
    );
    expect(await photoUpdatePage.getThumbnailx1Sha1Input()).to.eq(
      'fC3eDBCa0C1aC6e158aBEb04ea69b2aa94dbc079',
      'Expected Thumbnailx1Sha1 value to be equals to fC3eDBCa0C1aC6e158aBEb04ea69b2aa94dbc079'
    );
    expect(await photoUpdatePage.getThumbnailx2Input()).to.endsWith(
      fileNameToUpload,
      'Expected Thumbnailx2 value to be end with ' + fileNameToUpload
    );
    expect(await photoUpdatePage.getThumbnailx2Sha1Input()).to.eq(
      '0655fDb7D0fd053Fd3A6AbBE1ddfA660CDaC8Cd7',
      'Expected Thumbnailx2Sha1 value to be equals to 0655fDb7D0fd053Fd3A6AbBE1ddfA660CDaC8Cd7'
    );
    expect(await photoUpdatePage.getExifInput()).to.eq('exif', 'Expected Exif value to be equals to exif');
    expect(await photoUpdatePage.getExtractedTextInput()).to.eq(
      'extractedText',
      'Expected ExtractedText value to be equals to extractedText'
    );
    expect(await photoUpdatePage.getDetectedObjectsInput()).to.eq(
      'detectedObjects',
      'Expected DetectedObjects value to be equals to detectedObjects'
    );
    expect(await photoUpdatePage.getCreatedAtInput()).to.contain('2001-01-01T02:30', 'Expected createdAt value to be equals to 2000-12-31');
    expect(await photoUpdatePage.getUpdatedAtInput()).to.contain('2001-01-01T02:30', 'Expected updatedAt value to be equals to 2000-12-31');
    await photoUpdatePage.save();
    expect(await photoUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await photoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Photo', async () => {
    const nbButtonsBeforeDelete = await photoComponentsPage.countDeleteButtons();
    await photoComponentsPage.clickOnLastDeleteButton();

    photoDeleteDialog = new PhotoDeleteDialog();
    expect(await photoDeleteDialog.getDialogTitle()).to.eq('lazyblobApp.photo.delete.question');
    await photoDeleteDialog.clickOnConfirmButton();

    expect(await photoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
