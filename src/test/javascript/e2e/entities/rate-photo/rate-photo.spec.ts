import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { RateComponentsPage, RateDeleteDialog, RateUpdatePage } from './rate-photo.page-object';

const expect = chai.expect;

describe('Rate e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rateComponentsPage: RateComponentsPage;
  let rateUpdatePage: RateUpdatePage;
  let rateDeleteDialog: RateDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Rates', async () => {
    await navBarPage.goToEntity('rate-photo');
    rateComponentsPage = new RateComponentsPage();
    await browser.wait(ec.visibilityOf(rateComponentsPage.title), 5000);
    expect(await rateComponentsPage.getTitle()).to.eq('lazyblobApp.rate.home.title');
  });

  it('should load create Rate page', async () => {
    await rateComponentsPage.clickOnCreateButton();
    rateUpdatePage = new RateUpdatePage();
    expect(await rateUpdatePage.getPageTitle()).to.eq('lazyblobApp.rate.home.createOrEditLabel');
    await rateUpdatePage.cancel();
  });

  it('should create and save Rates', async () => {
    const nbButtonsBeforeCreate = await rateComponentsPage.countDeleteButtons();

    await rateComponentsPage.clickOnCreateButton();
    await promise.all([
      rateUpdatePage.setRateInput('5'),
      rateUpdatePage.setCreatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      rateUpdatePage.setUpdatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      rateUpdatePage.photoSelectLastOption(),
      rateUpdatePage.fromSelectLastOption()
    ]);
    expect(await rateUpdatePage.getRateInput()).to.eq('5', 'Expected rate value to be equals to 5');
    expect(await rateUpdatePage.getCreatedAtInput()).to.contain('2001-01-01T02:30', 'Expected createdAt value to be equals to 2000-12-31');
    expect(await rateUpdatePage.getUpdatedAtInput()).to.contain('2001-01-01T02:30', 'Expected updatedAt value to be equals to 2000-12-31');
    await rateUpdatePage.save();
    expect(await rateUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await rateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Rate', async () => {
    const nbButtonsBeforeDelete = await rateComponentsPage.countDeleteButtons();
    await rateComponentsPage.clickOnLastDeleteButton();

    rateDeleteDialog = new RateDeleteDialog();
    expect(await rateDeleteDialog.getDialogTitle()).to.eq('lazyblobApp.rate.delete.question');
    await rateDeleteDialog.clickOnConfirmButton();

    expect(await rateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
