package mda.generator.readers.xmi;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class XmiUtil {
	private static final Logger LOG = LogManager.getLogger(XmiUtil.class); 
	
	private XmiUtil() {
		
	}
	
	/**
	 * Récupération, depuis une node, des enfants qui ont le nom de balise fourni
	 * @param parentNode
	 * @param expectedName
	 * @return
	 */
	public static List<Node> getChildsWithTagName(Node parentNode, String expectedName) {
		return getChildsWithTagNameAndType(parentNode, expectedName, null);
	}

	/**
	 * Récupération, depuis une node, des enfants qui ont le nom de balise fourni et le type xmi:type fourni
	 * @param parentNode
	 * @param expectedName
	 * @param expectedType
	 * @return
	 */
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

	/**
	 * Récupère, pour une node, le 1er enfant avec le nom de balise fourni
	 * @param parentNode
	 * @param expectedName
	 * @return
	 */
	public static Node getFirstChildsNodeWithTagName(Node parentNode, String expectedName) {
		return getFirstChildsNodeWithTagNameAndType(parentNode, expectedName, null);
	}

	
	/**
	 * Récupère, pour une node, le 1er enfant avec le nom de balise fourni et le type xmi:type fourni
	 * @param parentNode
	 * @param expectedName
	 * @param expectedType
	 * @return
	 */
	public static Node getFirstChildsNodeWithTagNameAndType(Node parentNode, String expectedName, XmiElementType expectedType) {
		List<Node> nodeList = getChildsWithTagNameAndType(parentNode, expectedName, expectedType);

		if(nodeList.isEmpty()) {
			return null;
		}
		else {
			return nodeList.get(0);
		}
	}
	
	/**
	 * Valeur de l'attribut  xmi:type d'une node
	 * @param node
	 * @return
	 */
	public static String getElementType(Node node) {
		return getAttribute(node, "xmi:type");
	}

	/**
	 * Valeur de l'attribut  xmi:id d'une node
	 * @param node
	 * @return
	 */
	public static String getElementId(Node node) {
		return getAttribute(node, "xmi:id");
	}

	/**
	 * Valeur de l'attribut  xmi:idref d'une node
	 * @param node
	 * @return
	 */
	public static String getElementIdRef(Node node) {
		return getAttribute(node, "xmi:idref");
	}


	/**
	 * Valeur de l'attribut name d'une node
	 * @param node
	 * @return
	 */
	public static String getElementName(Node node) {
		return getAttribute(node, "name");
	}

	/**
	 * Valeur de l'attribut body d'une node
	 * @param node
	 * @return
	 */
	public static String getElementBody(Node node) {
		return getAttribute(node, "body");
	}
	
	/**
	 * Valeur de l'attribut isReadonly d'une node
	 * @param node
	 * @return
	 */
	public static Boolean getAttributeIsReadonly(Node node) {
		return getAttributeAsBool(node, "isReadOnly");
	}

	/**
	 * Récupère, pour une node, la valeur de l'attribut avec le nom fourni
	 * @param node
	 * @return
	 */
	public static String getAttribute(Node node, String attributeName) {
		Node namedItem = node.getAttributes().getNamedItem(attributeName);
		if(namedItem != null) {
			return namedItem.getNodeValue();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Récupère, pour une node, la valeur de l'attribut avec le nom fourni, en tant que booleéan. Null si non trouvé ou incorrect ("true" ou "false").
	 * @param node
	 * @param attributeName
	 * @return
	 */
	private static Boolean getAttributeAsBool(Node node, String attributeName) {
		String returnVal = getAttribute(node, attributeName);
		if(returnVal == null) {
			return null;
		}
		
		if("true".equalsIgnoreCase(returnVal)) {
			return true;
		} else if("false".equalsIgnoreCase(returnVal)) {
			return false;
		}
		
		LOG.error("Impossible de convertir l'attribut " + attributeName + " en boolean avec la valeur " + returnVal);		
		return null;
	}

}
