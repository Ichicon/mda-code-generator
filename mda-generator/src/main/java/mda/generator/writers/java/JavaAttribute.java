package mda.generator.writers.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mda.generator.beans.UmlAssociation;
import mda.generator.beans.UmlAttribute;
import mda.generator.converters.ConverterInterface;

/**
 * Class representing a java class attribute
 * @author Fabien Crapart
 */
public class JavaAttribute {
	private final Visibility visibility = Visibility.PRIVATE;
	private final String name;
	private final String columnName;
	private final List<String> comments = new ArrayList<>(); 
	private final String javaType;
	private final boolean isNotNull;
	private final boolean isPK;
	
	private String defaultValue;
	
	private static List<String> PRIMITIVE_TYPES = new ArrayList<>();
	static {	
		PRIMITIVE_TYPES.add("String");
		PRIMITIVE_TYPES.add("Long");
		PRIMITIVE_TYPES.add("Integer");
		PRIMITIVE_TYPES.add("Boolean");
		PRIMITIVE_TYPES.add("Double");
		PRIMITIVE_TYPES.add("Float");
		PRIMITIVE_TYPES.add("LocalDateTime");
		PRIMITIVE_TYPES.add("LocalDate");
		PRIMITIVE_TYPES.add("Blob");
	}


	/**
	 * Special field (like serial id)
	 * @param name
	 * @param type
	 */
	public JavaAttribute(String name,String type, String defaultValue) {
		this.name=name;
		this.columnName = null;
		this.javaType= type;
		this.isPK = false;
		this.isNotNull =false;
		this.defaultValue =defaultValue;
	}

	/**
	 * Creation of java attribute
	 * @param umlAttribute Uml attribute definition
	 * @param converter typeConverter
	 */
	public JavaAttribute(UmlAttribute umlAttribute, ConverterInterface converter, ImportManager importManager) {
		this.isPK = umlAttribute.isPK();
		this.javaType= importManager.getFinalName(converter.getJavaType(umlAttribute.getDomain()));
		this.name = umlAttribute.getCamelCaseName();
		this.columnName = umlAttribute.getName();

		if(umlAttribute.getComment() != null) {
			this.comments.addAll(Arrays.asList(umlAttribute.getComment().split("\n")));
		} else {
			comments.add(JavaWriter.NO_COMMENT_FOUND);
		}

		this.isNotNull = umlAttribute.getIsNotNull();
	}

	/**
	 * Attribute from association
	 * @param umlAssociation
	 * @param converter
	 * @param importManager
	 */
	public JavaAttribute(UmlAssociation umlAssociation, ConverterInterface converter, ImportManager importManager) {
		this.isPK =false;
		this.columnName = null;
			
		String javaTypeName = umlAssociation.getTarget().getXmiPackage().getName() + "." + umlAssociation.getTarget().getCamelCaseName();
		if(umlAssociation.isTargetMultiple()) {
			this.name = NamesComputingUtil.computeFkObjectName(umlAssociation)+ "List";
			this.javaType = importManager.getFinalName("java.util.Set") + "<" + importManager.getFinalName(javaTypeName) + ">";
		} else {
			this.name = NamesComputingUtil.computeFkObjectName(umlAssociation);
			this.javaType = importManager.getFinalName(javaTypeName);
		}

		this.isNotNull = umlAssociation.isTargetNullable();

		this.comments.add("Association " + umlAssociation.getName() + " to " + umlAssociation.getTarget().getCamelCaseName());
	}

	/**
	 * @return the visibility
	 */
	public Visibility getVisibility() {
		return visibility;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the javaType
	 */
	public String getJavaType() {
		return javaType;
	}

	/**
	 * @return the isNotNull
	 */
	public boolean isNotNull() {
		return isNotNull;
	}

	/**
	 * @return the comment
	 */
	public List<String> getComments() {
		return comments;
	}

	/**
	 * @return the isPK
	 */
	public boolean isPK() {
		return isPK;
	}

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	
	/**
	 * Indique si le type est primitif
	 * @return
	 */
	public boolean isPrimitive(){
		return PRIMITIVE_TYPES.contains(this.javaType);
	}
	
}
