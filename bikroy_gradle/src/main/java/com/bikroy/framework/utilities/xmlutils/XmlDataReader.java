package com.bikroy.framework.utilities.xmlutils;

import java.util.HashMap;
import java.util.Map;

import com.bikroy.framework.globals.GlobalConsts;
import com.bikroy.framework.utilities.textutilities.TextUtilities;

class XmlDataReader {
	DomParser dp;

	protected XmlDataReader(final String xmlFileName) throws Exception {
		this._InitializeDomParser(GlobalConsts.DirectoryPathTestData, xmlFileName);
	}

	protected XmlDataReader(final String xmlDir, final String xmlFileName) throws Exception {
		this._InitializeDomParser(xmlDir, xmlFileName);
	}

	private void _InitializeDomParser(final String xmlDir, final String xmlFileName) throws Exception {
		this.dp = new DomParser(xmlDir, xmlFileName);
	}

	protected String readData(String dataSetName, String keyName, String category) throws Exception {
		HashMap<String, String> ln = this.readData(dataSetName, category);

		String value = "";
		for (Map.Entry<String, String> entry : ln.entrySet()) {
			String key = entry.getKey();
			if (TextUtilities.equalsIgnoreCase(key, keyName)) {
				value = entry.getValue();
				if (value.contains("{RANDOM}")) {
					value = value.replace("{RANDOM}", "") + TextUtilities.getRandomString(7);
				}
				if (value.contains("{RANDOMNUM}")) {
					value = value.replace("{RANDOMNUM}", "") + TextUtilities.getRandomInteger(10000, 99999);
				}
			}
		}

		return value.trim();
	}

	protected HashMap<String, String> readData(String dataSetName, String category) throws Exception {
		HashMap<String, String> ln = this.dp.getTestDataSetNodesWithValues(GlobalConsts.TestEnvironment, dataSetName, category);
		return ln;
	}
}