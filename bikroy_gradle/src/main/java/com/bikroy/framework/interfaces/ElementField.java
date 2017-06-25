package com.bikroy.framework.interfaces;

/**
 * ElementField objective to parse each element xml/database info to an object
 * form.
 * 
 * @author Aishwarya Dwivedi
 * @since 1.0
 * @version 1.0
 */
public class ElementField {
	public String FieldId;

	public String elementLocatorId = "";

	public String elementLocatorName = "";

	public String elementLocatorXpath = "";

	public String elementLocatorCss = "";

	public String elementLocatorCustomAttr = "";

	public String elementLocatorCustomAttrVal = "";

	public boolean elementLocatorWaitEligible = true;

	public int elementLocatorWaitTime = 0;

	public int elementLocatorDynamicIndex = -9999;

	public boolean isMandatory = false;
}
