package mda.generator.readers.xmi;

import java.util.HashMap;
import java.util.Map;

/**
 * Types d'éléments rencontrés dans le ficheir XMI
 * @author Fabien
 *
 */
public enum XmiElementType {
	PACKAGE("uml:Package"), // Package
	COMMENT("uml:Comment"), // Commentaire sur un diagramme (va dans le package)
	CLASS("uml:Class"), // Classe/table
	PROPERTY("uml:Property"), // property <=> attribute
	ASSOCIATION("uml:Association"), // Association entre deux classes
	DOMAIN("uml:PrimitiveType"); // Définition d'un domaine
	
	private static Map<String, XmiElementType> mapPerName = new HashMap<>();
	static {
		for(XmiElementType elt : values()) {
			mapPerName.put(elt.name, elt);
		}
	}
	
	private final String name;
	XmiElementType(String name) {
		this.name = name;		
	}
		
	/**
	 * @return the name
	 */
	public String getXmiName() {
		return name;
	}



	public static XmiElementType getByName(String name) {
		return mapPerName.get(name);
	}
	
}
