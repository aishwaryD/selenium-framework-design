package com.bikroy.framework.pageclasses;

import com.bikroy.framework.globals.GlobalController;
import com.bikroy.framework.interfaces.ElementField;
import com.bikroy.framework.interfaces.LocateBy;
import com.bikroy.framework.utilities.xmlutils.TestData;

public class CityDivisionPage {

	@LocateBy(xpath = "(//a[contains(@href,'/post-ad/location?type=for_sale&category=')])[2]")
	private ElementField HL_DhakaLink;

	@LocateBy(xpath = "//select[@id = 'locations-1211']/option")
	private ElementField DD_City;

	/**
	 * Select City
	 * 
	 * @return
	 * @throws Exception
	 */
	public CityDivisionPage clickDhakaCityLink() throws Exception {
		GlobalController.brw.click(this.HL_DhakaLink);
		return GlobalController.brw.initElements(CityDivisionPage.class);
	}

	/**
	 * Select Sub Category
	 * 
	 * @param testDataSetName
	 * @param category
	 * @return page object
	 * @throws Exception
	 */
	public AdDetailsPage selectCityFromDropdown(String testDataSetName, String category) throws Exception {
		String city = TestData.read("CityDivisionPageData.xml", testDataSetName, "city", category);
		GlobalController.brw.selectListItem(this.DD_City, city);
		return GlobalController.brw.initElements(AdDetailsPage.class);
	}
}
