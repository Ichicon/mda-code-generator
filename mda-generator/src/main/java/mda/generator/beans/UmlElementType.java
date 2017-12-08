package mda.generator.beans;

import java.util.HashMap;
import java.util.Map;

public enum UmlElementType {
	PACKAGE("uml:Package"), // Package
	COMMENT("uml:Comment"), // Commentaire sur un diagramme (va dans le package)
	CLASS("uml:Class"), // Classe/table
	PROPERTY("uml:Property"), // property <=> attribute
	ASSOCIATION("uml:Association"), // Association entre deux classes
	DOMAIN("uml:PrimitiveType"); // Définition d'un domaine
	
	private static Map<String, UmlElementType> mapPerName = new HashMap<>();
	static {
		for(UmlElementType elt : values()) {
			mapPerName.put(elt.name, elt);
		}
	}
	
	private final String name;
	UmlElementType(String name) {
		this.name = name;		
	}
		
	/**
	 * @return the name
	 */
	public String getXmiName() {
		return name;
	}



	public static UmlElementType getByName(String name) {
		return mapPerName.get(name);
	}
	
}
