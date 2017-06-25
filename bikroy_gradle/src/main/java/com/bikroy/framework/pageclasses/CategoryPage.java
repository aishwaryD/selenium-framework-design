package com.bikroy.framework.pageclasses;

import com.bikroy.framework.globals.GlobalController;
import com.bikroy.framework.interfaces.ElementField;
import com.bikroy.framework.interfaces.LocateBy;

public class CategoryPage {

	@LocateBy(xpath = "//a[contains(@href,'/post-ad/category?type=for_sale&category=')]//span[contains(@class,electronics)]")
	private ElementField HL_Electronics;

	@LocateBy(xpath = "//a[contains(@href,'/post-ad/category?type=for_sale&category=851')]")
	private ElementField HL_TVs;

	/**
	 * Click Electronics Link
	 * 
	 * @return
	 * @throws Exception
	 */
	public CategoryPage clickElectronicsCategory() throws Exception {
		GlobalController.brw.click(this.HL_Electronics);
		return GlobalController.brw.initElements(CategoryPage.class);
	}

	/**
	 * This method will click on TV link
	 * 
	 * @return
	 * @throws Exception
	 */
	public CityDivisionPage clickTVsSubCategory() throws Exception {
		GlobalController.brw.clickHiddenHref(this.HL_TVs);
		return GlobalController.brw.initElements(CityDivisionPage.class);
	}

}
