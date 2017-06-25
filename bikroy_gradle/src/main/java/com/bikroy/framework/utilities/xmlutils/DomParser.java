package com.bikroy.framework.utilities.xmlutils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bikroy.framework.globals.GlobalConsts;
import com.bikroy.framework.globals.Logger;
import com.bikroy.framework.utilities.fileutils.FileUtilities;
import com.bikroy.framework.utilities.textutilities.TextUtilities;

class DomParser {
	// Initialize private logger object
	private static Logger logger = new Logger().getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	private Document doc = null;

	protected DomParser(final String xmlDir, final String xmlFileName) throws Exception {
		try {
			String xmlFileNameWithPath = xmlDir + xmlFileName;
			if (TextUtilities.isBlank(xmlFileNameWithPath)) {
				throw new Exception("No XML file name provided to read");
			}

			if (FileUtilities.FileExists(xmlFileNameWithPath) == false) {
				throw new Exception("No such XML file exists to read: " + xmlFileNameWithPath);
			}

			File inputFile = new File(xmlFileNameWithPath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			this.doc = dBuilder.parse(inputFile);
			this.doc.getDocumentElement().normalize();
			DomParser.logger.info("Root element :" + this.doc.getDocumentElement().getNodeName());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	protected Document getDocument() {
		return this.doc;
	}

	protected Node getNode(String tagName, NodeList nodes) {
		if (nodes == null) {
			return null;
		}

		for (int x = 0; x < nodes.getLength(); x++) {
			Node node = nodes.item(x);
			if (node.getNodeName().equalsIgnoreCase(tagName)) {
				return node;
			}
		}

		return null;
	}

	protected String getNodeAttr(String attrName, Node node) {
		NamedNodeMap attrs = node.getAttributes();
		for (int y = 0; y < attrs.getLength(); y++) {
			Node attr = attrs.item(y);
			if (attr.getNodeName().equalsIgnoreCase(attrName)) {
				return attr.getNodeValue();
			}
		}
		return "";
	}

	protected String getNodeAttr(String tagName, String attrName, NodeList nodes) {
		for (int x = 0; x < nodes.getLength(); x++) {
			Node node = nodes.item(x);
			if (node.getNodeName().equalsIgnoreCase(tagName)) {
				NodeList childNodes = node.getChildNodes();
				for (int y = 0; y < childNodes.getLength(); y++) {
					Node data = childNodes.item(y);
					if (data.getNodeType() == Node.ATTRIBUTE_NODE) {
						if (data.getNodeName().equalsIgnoreCase(attrName)) {
							return data.getNodeValue();
						}
					}
				}
			}
		}

		return "";
	}

	/**
	 * 
	 * @param nodeNameWithPath
	 *            E.g. /class/student
	 * @return
	 */
	protected NodeList getNodes(String tagName) {
		NodeList nodesList = this.doc.getElementsByTagName(tagName);
		DomParser.logger.info("Nodes found with " + tagName + " = " + nodesList.getLength());
		return nodesList;
	}

	protected String getNodeValue(Node node) {
		NodeList childNodes = node.getChildNodes();
		for (int x = 0; x < childNodes.getLength(); x++) {
			Node data = childNodes.item(x);
			if (data.getNodeType() == Node.TEXT_NODE) {
				return data.getNodeValue();
			}
		}
		return "";
	}

	protected String getNodeValue(String tagName, NodeList nodes) {
		if ((nodes == null) || TextUtilities.isBlank(tagName)) {
			return null;
		}

		for (int x = 0; x < nodes.getLength(); x++) {
			Node node = nodes.item(x);
			if (node.getNodeName().equalsIgnoreCase(tagName)) {
				NodeList childNodes = node.getChildNodes();
				for (int y = 0; y < childNodes.getLength(); y++) {
					Node data = childNodes.item(y);
					if (data.getNodeType() == Node.TEXT_NODE) {
						return data.getNodeValue();
					}
				}
			}
		}
		return "";
	}

	protected String getSingleNodeValue(NodeList nodesList, String tagName) {
		String nodeValue = null;
		for (int temp = 0; temp < nodesList.getLength(); temp++) {
			Node nd = nodesList.item(temp);
			if (TextUtilities.equalsIgnoreCase(nd.getNodeName(), tagName)) {
				nodeValue = nd.getTextContent();
				break;
			}
		}

		return nodeValue;
	}

	protected String getSingleNodeValue(String tagName) {
		String loc = this.doc.getElementsByTagName(tagName).item(0).getTextContent();
		loc = loc.trim();
		return loc;
	}

	protected List<Node> getTestDataSetNodes(String dataSetName, String category) throws Exception {
		return this.getTestDataSetNodes(GlobalConsts.TestEnvironment, dataSetName, category);
	}

	protected NodeList getTestDataSetNodesList(String testEnv, String dataSetName, String category) {
		NodeList nodes = this.getNodes("testdataset");
		NodeList eligibleNodesList = null;

		for (int temp = 0; temp < nodes.getLength(); temp++) {
			Node nd = nodes.item(temp);
			DomParser.logger.info("Checking node [" + nd.getNodeName() + "] at index " + temp);

			if (nd.getNodeType() == Node.ELEMENT_NODE) {
				String env = this.getNodeAttr("testenv", nd);
				String dsName = this.getNodeAttr("name", nd);
				String cat = this.getNodeAttr("category", nd);
				DomParser.logger.info(env + " -- " + dsName + " -- " + cat);
				if (TextUtilities.contains(env, testEnv) && TextUtilities.equalsIgnoreCase(dsName, dataSetName)
						&& TextUtilities.equalsIgnoreCase(cat, category)) {
					eligibleNodesList = nd.getChildNodes();
					break;
				}
			}
		}

		if (eligibleNodesList == null) {
			return null;
		}

		return eligibleNodesList;
	}

	protected List<Node> getTestDataSetNodes(String testEnv, String dataSetName, String category) throws Exception {
		DomParser.logger.enterMethod();
		NodeList eligibleNodesList = this.getTestDataSetNodesList(testEnv, dataSetName, category);

		List<Node> nl = new ArrayList<Node>();
		DomParser.logger.info(eligibleNodesList.getLength());
		for (int temp = 0; temp < eligibleNodesList.getLength(); temp++) {
			Node nd = eligibleNodesList.item(temp);
			if (nd.getNodeType() == Node.TEXT_NODE) {
				DomParser.logger.info("text node found; skipping this node");
				continue;
			}

			DomParser.logger.info(nd.getNodeName() + " <<---->>  " + this.getNodeValue(nd) + " <<---->> " + nd.getNodeType());
			nl.add(nd);
		}

		DomParser.logger.exitMethod();
		return nl;
	}

	protected HashMap<String, String> getTestDataSetNodesWithValues(String testEnv, String dataSetName, String category) throws Exception {
		DomParser.logger.enterMethod();
		NodeList eligibleNodesList = this.getTestDataSetNodesList(testEnv, dataSetName, category);
		if (eligibleNodesList == null) {
			return null;
		}

		HashMap<String, String> nl = new HashMap<String, String>();
		DomParser.logger.info(eligibleNodesList.getLength());
		for (int temp = 0; temp < eligibleNodesList.getLength(); temp++) {
			Node nd = eligibleNodesList.item(temp);
			if (nd.getNodeType() == Node.TEXT_NODE) {
				DomParser.logger.info("text node found; skipping this node");
				continue;
			}

			DomParser.logger.info(nd.getNodeName() + " <<---->>  " + this.getNodeValue(nd) + " <<---->> " + nd.getNodeType());
			nl.put(nd.getNodeName(), this.getNodeValue(nd));
		}

		DomParser.logger.exitMethod();
		return nl;
	}
}
