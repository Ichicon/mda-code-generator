package mda.generator.readers.xmi;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class XmiUtil {

	private XmiUtil() {
		
	}
	
	public static List<Node> getChildsWithTagName(Node parentNode, String expectedName) {
		return getChildsWithTagNameAndType(parentNode, expectedName, null);
	}


	public static List<Node> getChildsWithTagNameAndType(Node parentNode, String expectedName, XmiElementType expectedType) {
		List<Node> nodeList = new ArrayList<>();

		if(parentNode.hasChildNodes()) {
			NodeList childs = parentNode.getChildNodes();
			for (int idx = 0; idx < childs.getLength(); idx++) {    	
				Node child = childs.item(idx);
				if (child.getNodeType() == Node.ELEMENT_NODE) {
					if(expectedName.equals(child.getNodeName()) && (expectedType == null || expectedType.getXmiName().equals(getElementType(child)))) {
						nodeList.add(child);
					}
				}
			}
		}

		return nodeList;
	}

	public static Node getFirstChildsNodeWithTagName(Node parentNode, String expectedName) {
		return getFirstChildsNodeWithTagNameAndType(parentNode, expectedName, null);
	}

	public static Node getFirstChildsNodeWithTagNameAndType(Node parentNode, String expectedName, XmiElementType expectedType) {
		List<Node> nodeList = getChildsWithTagNameAndType(parentNode, expectedName, expectedType);

		if(nodeList.isEmpty()) {
			return null;
		}
		else {
			return nodeList.get(0);
		}
	}


	public static String getElementType(Node node) {
		return getAttribute(node, "xmi:type");
	}

	public static String getElementId(Node node) {
		return getAttribute(node, "xmi:id");
	}

	public static String getElementIdRef(Node node) {
		return getAttribute(node, "xmi:idref");
	}


	public static String getElementName(Node node) {
		return getAttribute(node, "name");
	}

	public static String getElementBody(Node node) {
		return getAttribute(node, "body");
	}

	public static String getAttribute(Node node, String attributeName) {
		Node namedItem = node.getAttributes().getNamedItem(attributeName);
		if(namedItem != null) {
			return namedItem.getNodeValue();
		}
		else {
			return null;
		}
	}

}
