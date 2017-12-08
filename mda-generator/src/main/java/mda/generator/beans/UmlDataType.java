package mda.generator.beans;

import java.util.HashMap;
import java.util.Map;

public enum UmlDataType {
	LONG("UnlimitedNatural"),
	STRING("String"),
	BOOLEAN("Boolean"),
	INTEGER("Integer");
	
	private static final Map<String, UmlDataType> mapPerName = new HashMap<>();
	static {
		for(UmlDataType xmiDataType : values()) {
			mapPerName.put(xmiDataType.xmiName, xmiDataType);
		}
	}
	
	private final String xmiName;	
	private UmlDataType(String xmiName) {
		this.xmiName = xmiName;
	}
	
	public static UmlDataType getByXmiName(String xmiName) {
		return mapPerName.get(xmiName);
	}
}
