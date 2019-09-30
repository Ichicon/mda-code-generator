package mda.generator.converters.type;

import mda.generator.beans.UmlDomain;

/**
 * Compute java type corresponding to the domain, can be extended to compute db type from this java type
 *
 * @author Fabien Crapart
 */
public abstract class AbstractDomainToJavaConverter implements TypeConverterInterface {
	@Override
	public String getJavaType(UmlDomain domain) {
		String finalType;

		// On corrige les éventuels problèmes de casse
		switch(domain.getTypeName().toLowerCase()) {
		case "string":
			finalType="String";
			break;
		case "boolean" :
		case "bool" :
			finalType= "Boolean";
			break;
		case "integer" :
		case "int" :
			finalType= "Integer";
			break;
		case "long":
			finalType= "Long";
			break;
		case "java.time.localdate":
		case "localdate":
			finalType= "java.time.LocalDate";
			break;
		case "java.time.localdatetime":
		case "localdatetime":
			finalType= "java.time.LocalDateTime";
			break;
		case "float":
			finalType= "Float";
			break;
		case "double":
			finalType= "Double";
			break;
		case "java.math.bigdecimal":
		case "bigdecimal":
			finalType= "java.math.BigDecimal";
			break;
		case "byte[]":
			finalType= "Byte[]";
			break;
		case "blob":
		case "java.sql.blob":
			finalType= "java.sql.Blob";
			break;
		default :
			finalType = domain.getTypeName();
			break;
		}

		return finalType;
	}

}
