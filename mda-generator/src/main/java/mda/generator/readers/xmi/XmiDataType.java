package mda.generator.readers.xmi;

import java.util.HashMap;
import java.util.Map;

/**
 * Liste des "dataTypes" rencontrés dans le schéma XMI
 * @author Fabien
 *
 */
public enum XmiDataType {
	LONG("UnlimitedNatural"),
	STRING("String"),
	BOOLEAN("Boolean"),
	INTEGER("Integer");
	
	private static final Map<String, XmiDataType> mapPerName = new HashMap<>();
	static {
		for(XmiDataType xmiDataType : values()) {
			mapPerName.put(xmiDataType.xmiName, xmiDataType);
		}
	}
	
	private final String xmiName;	
	private XmiDataType(String xmiName) {
		this.xmiName = xmiName;
	}
	
	public static XmiDataType getByXmiName(String xmiName) {
		return mapPerName.get(xmiName);
	}
}
