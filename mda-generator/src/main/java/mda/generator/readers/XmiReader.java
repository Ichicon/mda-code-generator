package mda.generator.readers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mda.generator.beans.UmlAssociation;
import mda.generator.beans.UmlAttribute;
import mda.generator.beans.UmlClass;
import mda.generator.beans.UmlDomain;
import mda.generator.beans.UmlElementType;
import mda.generator.beans.UmlPackage;

public class XmiReader implements ModelFileReader {
	private static Logger LOG = LogManager.getLogger();

	private Map<String, UmlAssociation> associationsMap = new HashMap<>(); // by id ?
	private Map<String, UmlClass> classesMap = new HashMap<>(); // by id
	private Map<String, UmlDomain> domainsMap = new HashMap<>();  // by name
	private Map<String, UmlPackage> packagesMap = new HashMap<>(); // by name

	/**
	 * @return the associationsMap
	 */
	public Map<String, UmlAssociation> getAssociationsMap() {
		return associationsMap;
	}

	/**
	 * @return the classesMap
	 */
	public Map<String, UmlClass> getClassesMap() {
		return classesMap;
	}

	/**
	 * @return the domainsMap
	 */
	public Map<String, UmlDomain> getDomainsMap() {
		return domainsMap;
	}

	/**
	 * @return the packagesMap
	 */
	public Map<String, UmlPackage> getPackagesMap() {
		return packagesMap;
	}

	public void extractObjects(String pathToXmi) {
		LOG.info("Lecture du fichier " + pathToXmi);

		try {    		
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(pathToXmi));

			// Analyse des packages et domaines
			parcourirPackagesEtDomaines(doc);

			// On parcours classes et attributs
			parcourirClasses(doc);


			//Associations tagName = connector , 

		} catch (Exception e) {
			LOG.error("Erreur lors du parsing du fichier XMI", e);
		}

	}

	/**
	 * Permet d'exdtraire les packages et les domaines
	 * @param doc
	 */
	private void parcourirPackagesEtDomaines(Document doc) {
		NodeList elts = doc.getDocumentElement().getElementsByTagName("packagedElement");
		for (int idx = 0; idx < elts.getLength(); idx++) {    	
			Node currNode = elts.item(idx);
			if (currNode.getNodeType() == Node.ELEMENT_NODE) {
				UmlElementType type = UmlElementType.getByName(getElementType(currNode));
				try {
					switch(type) {
					case PACKAGE :
						// On ne traite que les packages java
						String name = getElementName(currNode);
						if(name.contains(".")) {
							extrairePackage(currNode);
						}

						break;
					case DOMAIN :
						extraireDomaine(currNode);
					default:
						break;
					}
				} catch(Exception e) {
					LOG.error("Type non reconnnu = '" +  getElementType(currNode)+"'", e);
				}
			}
		}
	}

	protected void extrairePackage(Node packageNode) {
		UmlPackage xmiPackage = new UmlPackage();
		xmiPackage.setEaId(getElementId(packageNode));
		xmiPackage.setName(getElementName(packageNode));

		// On cherche les commentaires
		List<Node>  commentaires = getChildsWithTagNameAndType(packageNode, "ownedComment", UmlElementType.COMMENT);
		if(!commentaires.isEmpty()) {
			StringBuilder sbCommentaires = new StringBuilder();
			for(Node commentaire : commentaires) {
				sbCommentaires.append(getElementBody(commentaire)).append("\n");
			}
			xmiPackage.setComment(sbCommentaires.toString());
		}	

		// Ajout dans la map des packages par nom
		packagesMap.put(xmiPackage.getName(), xmiPackage);
	}

	protected void extraireDomaine(Node domainNode) {
		UmlDomain xmiDomain = new UmlDomain();
		xmiDomain.setName(getElementName(domainNode));


		// Ajout dans la map des domaines
		domainsMap.put(xmiDomain.getName(), xmiDomain);
	}

	/**
	 * Parcour des classes
	 * @param doc
	 */
	protected void parcourirClasses(Document doc) {
		Node xmiExtension =  doc.getElementsByTagName("xmi:Extension").item(0);
		Node elements = getFirstChildsNodeWithTagNameAndType(xmiExtension,"elements", null);

		List<Node> elts = getChildsWithTagNameAndType(elements, "element", UmlElementType.CLASS);
		for (Node currNode : elts) {    	
			if (currNode.getNodeType() == Node.ELEMENT_NODE) {
				UmlElementType type = UmlElementType.getByName(getElementType(currNode));
				try {
					switch(type) {
					case CLASS :
						extraireClasse(currNode);
						break;						
					default:
						break;
					}
				} catch(Exception e) {
					LOG.error("Type non reconnnu = '" +  getElementType(currNode)+"'",e);
				}
			}
		}



	}

	protected void extraireClasse(Node classNode) {
		UmlClass xmiClass = new UmlClass();
		xmiClass.setEaId(getElementIdRef(classNode));
		xmiClass.setName(getElementName(classNode));

		// Package extendedProperties tagged="0" package_name="fr.gouv.mindef.gestim.domain.messages"/><attributes>
		Node extendedProperties = getFirstChildsNodeWithTagName(classNode, "extendedProperties");
		String packageName = getAttribute(extendedProperties, "package_name");
		UmlPackage xmiPackage = packagesMap.get(packageName);
		xmiPackage.getClasses().add(xmiClass);
		xmiClass.setXmiPackage(xmiPackage);

		// Commentaires <properties documentation="Message à destination des CSNs" i
		Node properties = getFirstChildsNodeWithTagName(classNode, "properties");
		if(properties != null) {
			xmiClass.setComment(getAttribute(properties, "documentation"));
		}

		// Attributs
		Node attributes = getFirstChildsNodeWithTagName(classNode, "attributes");
		if(attributes != null && attributes.hasChildNodes()) {
			for(Node attribut : getChildsWithTagName(attributes, "attribute")) {
				extractAttribute(attribut, xmiClass);
			}
		}

		// Ajout dans la map des classes
		classesMap.put(xmiClass.getEaId(), xmiClass);
	}

	private void extractAttribute(Node attribut, UmlClass xmiClass) {
		UmlAttribute xmiAttribut = new UmlAttribute();
		xmiAttribut.setName(getElementName(attribut));
		// Domain ex:  <properties type="DO_ID" derived="0" collection="false" duplicates="0" changeability="changeable"/>
		Node properties = getFirstChildsNodeWithTagName(attribut, "properties");
		String domainName = getAttribute(properties, "type");
		xmiAttribut.setDomain(domainsMap.get(domainName));

		// Commentaire ex : <style value="Identifiant technique de l'utilisateur"/>
		Node style = getFirstChildsNodeWithTagName(attribut, "style");
		xmiAttribut.setComment(getAttribute(style, "value"));

		// Not null ex :  <bounds lower="1" upper="1"/>
		Node bounds = getFirstChildsNodeWithTagName(attribut, "bounds");
		xmiAttribut.setIsNotNull(!"0".equals(getAttribute(bounds, "lower")));

		// PK ? ex :  <xrefs value="$XREFPROP=$XID={3C6F55AC-762C-4339-AEA5-6B85C4EEFAB8}$XID;$NAM=CustomProperties$NAM;$TYP=attribute property$TYP;$VIS=Public$VIS;$PAR=0$PAR;$DES=@PROP=@NAME=isID@ENDNAME;@TYPE=Boolean@ENDTYPE;@VALU=1@ENDVALU;@PRMT=@ENDPRMT;@ENDPROP;$DES;$CLT={C11171CB-49AD-4ae1-97B5-32E527D973EB}$CLT;$SUP=<none>$SUP;$ENDXREF;"/>
		Node xrefs = getFirstChildsNodeWithTagName(attribut, "xrefs");
		String xrefsVals = getAttribute(xrefs, "value");
		xmiAttribut.setPK(xrefsVals != null && xrefsVals.contains("@NAME=isID@ENDNAME"));
		
		// Ajout de l'attribut dans la classe
		xmiClass.getAttributs().add(xmiAttribut);
	}


	private List<Node> getChildsWithTagName(Node parentNode, String expectedName) {
		return getChildsWithTagNameAndType(parentNode, expectedName, null);
	}


	private List<Node> getChildsWithTagNameAndType(Node parentNode, String expectedName, UmlElementType expectedType) {
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

	private Node getFirstChildsNodeWithTagName(Node parentNode, String expectedName) {
		return getFirstChildsNodeWithTagNameAndType(parentNode, expectedName, null);
	}

	private Node getFirstChildsNodeWithTagNameAndType(Node parentNode, String expectedName, UmlElementType expectedType) {
		List<Node> nodeList = getChildsWithTagNameAndType(parentNode, expectedName, expectedType);

		if(nodeList.isEmpty()) {
			return null;
		}
		else {
			return nodeList.get(0);
		}
	}


	private String getElementType(Node node) {
		return getAttribute(node, "xmi:type");
	}

	private String getElementId(Node node) {
		return getAttribute(node, "xmi:id");
	}

	private String getElementIdRef(Node node) {
		return getAttribute(node, "xmi:idref");
	}


	private String getElementName(Node node) {
		return getAttribute(node, "name");
	}

	private String getElementBody(Node node) {
		return getAttribute(node, "body");
	}

	private String getAttribute(Node node, String attributeName) {
		Node namedItem = node.getAttributes().getNamedItem(attributeName);
		if(namedItem != null) {
			return namedItem.getNodeValue();
		}
		else {
			return null;
		}
	}




}