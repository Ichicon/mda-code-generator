package mda.generator.writers.java.codepart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mda.generator.beans.UmlAssociation;
import mda.generator.beans.UmlAttribute;
import mda.generator.converters.java.JavaNameConverterInterface;
import mda.generator.converters.type.TypeConverterInterface;
import mda.generator.writers.java.JavaWriter;
import mda.generator.writers.java.utils.ImportManager;

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
	private final boolean isUpdatable;
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
		columnName = null;
		javaType= type;
		isPK = false;
		isNotNull =false;
		isUpdatable = true;
		this.defaultValue =defaultValue;
	}

	/**
	 * Creation of java attribute
	 * @param umlAttribute Uml attribute definition
	 * @param converter typeConverter
	 */
	public JavaAttribute(UmlAttribute umlAttribute, TypeConverterInterface converter,JavaNameConverterInterface javaNameConverter, ImportManager importManager) {
		isPK = umlAttribute.isPK();
		javaType= importManager.getFinalName(converter.getJavaType(umlAttribute.getDomain()));
		name = javaNameConverter.convertAttributeName(umlAttribute.getName());
		columnName = umlAttribute.getName();

		if(umlAttribute.getComment() != null) {
			comments.addAll(Arrays.asList(umlAttribute.getComment().split("\n")));
		} else {
			comments.add(JavaWriter.NO_COMMENT_FOUND);
		}

		isNotNull = umlAttribute.getIsNotNull();
		isUpdatable = umlAttribute.isReadonly() == null ? true : !umlAttribute.isReadonly() ;
	}

	/**
	 * Attribute from association
	 * @param umlAssociation
	 * @param converter
	 * @param importManager
	 */
	public JavaAttribute(UmlAssociation umlAssociation, TypeConverterInterface converter,JavaNameConverterInterface javaNameConverter, ImportManager importManager) {
		isPK =false;
		columnName = null;

		String targetClassCamelName = javaNameConverter.convertClassName(umlAssociation.getTarget().getName());

		String javaTypeName = umlAssociation.getTarget().getXmiPackage().getName() + "." + targetClassCamelName;
		name = javaNameConverter.computeFkAttributeName(umlAssociation);
		if(umlAssociation.isTargetMultiple()) {
			javaType = importManager.getFinalName("java.util.Set") + "<" + importManager.getFinalName(javaTypeName) + ">";
		} else {
			javaType = importManager.getFinalName(javaTypeName);
		}

		isNotNull = umlAssociation.isTargetNullable();
		isUpdatable  = true;

		comments.add("Association " + umlAssociation.getName() + " to " + targetClassCamelName);
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
		return PRIMITIVE_TYPES.contains(javaType);
	}

	/**
	 * @return the isUpdatable
	 */
	public boolean isUpdatable() {
		return isUpdatable;
	}

}
